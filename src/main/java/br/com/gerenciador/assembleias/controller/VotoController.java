package br.com.gerenciador.assembleias.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.gerenciador.assembleias.controller.dto.VotoDto;
import br.com.gerenciador.assembleias.controller.form.VotoForm;
import br.com.gerenciador.assembleias.model.Pauta;
import br.com.gerenciador.assembleias.model.ResultadoValidaUsuarioEnum;
import br.com.gerenciador.assembleias.model.Voto;
import br.com.gerenciador.assembleias.repository.PautaRepository;
import br.com.gerenciador.assembleias.repository.VotoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/voto")
public class VotoController {

	private static final String USER_VALIDA_CPF = "https://thiagobdp-usuarios-cpf.herokuapp.com/users/{cpf}";

	@Autowired
	PautaRepository pautaRepository;

	@Autowired
	VotoRepository votoRepository;

	@ApiOperation(value = "Lista todos os votos realziados")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Retorna a lista de Votos") })
	@GetMapping(produces = { "application/json" })
	public List<VotoDto> listar() {
		return votoRepository.findAll().stream().map(voto -> VotoDto.converter(voto)).collect(Collectors.toList());
	}

	/**
	 * Realiza do voto.
	 * 
	 * @param id
	 * @param votoForm
	 * @param uriBuilder
	 * @return
	 */
	@ApiOperation(value = "Realiza o voto de um usuário")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Alguma exceção é lançada por erro de negócio"),
			@ApiResponse(code = 404, message = "Pauta não foi encontrada"),
			@ApiResponse(code = 201, message = "Voto realizado com sucesso. Retorna o novo voto.") })
	@Transactional
	@PostMapping(consumes = { "application/json" }, value = "/votar", produces = { "application/json" })
	public ResponseEntity<VotoDto> votar(@RequestBody @Valid VotoForm votoForm, UriComponentsBuilder uriBuilder) {

		if (!this.isUsuarioAbleToVote(votoForm.getCpf())) {
			throw new IllegalStateException("Usuário CPF '" + votoForm.getCpf() + "' não tem permissão para votar.");
		}

		Optional<Pauta> opt = pautaRepository.findById(votoForm.getIdPauta());
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pauta pauta = opt.get();
		if (!pauta.isSessaoIniciada()) {
			throw new IllegalStateException("Não é possível votar pois a sessão ainda não foi aberta.");
		}

		if (pauta.getSessaoFechada()) {
			throw new IllegalStateException("Não é possível votar pois a sessão já está fechada.");
		}

		Voto voto = Voto.votar(votoForm, pauta);
		if (voto == null) {
			throw new IllegalStateException(
					"Não é possível votar pois o CPF:" + votoForm.getCpf() + " já realizou o voto.");
		}

		Voto votoSalvo = this.votoRepository.save(voto);
		URI uri = uriBuilder.path("/voto/{id}").buildAndExpand(votoSalvo.getId()).toUri();
		return ResponseEntity.created(uri).body(new VotoDto(voto));
	}

	private Boolean isUsuarioAbleToVote(String cpf) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> param = new HashMap<String, String>();
		param.put("cpf", cpf);

		HttpEntity<ResultadoValidaUsuarioEnum> entity = new HttpEntity<ResultadoValidaUsuarioEnum>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ResultadoValidaUsuarioEnum> result = null;
		try {
			result = restTemplate.exchange(USER_VALIDA_CPF, HttpMethod.GET, entity, ResultadoValidaUsuarioEnum.class,
					param);
//		} catch (RestClientException ex) {
//			System.out.println(ex.getMessage());
//			System.out.println(ex);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().compareTo(HttpStatus.NOT_FOUND) == 0) {
				throw new IllegalStateException("CPF: '" + cpf + "' é inválido");
			} else {
				throw e;
			}
		}

		if (ResultadoValidaUsuarioEnum.ABLE_TO_VOTE.compareTo(result.getBody()) == 0) {
			return true;
		} else {
			return false;
		}
	}
}
