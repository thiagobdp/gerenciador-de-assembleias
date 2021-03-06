package br.com.gerenciador.assembleias.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.gerenciador.assembleias.controller.dto.PautaDetalhesDto;
import br.com.gerenciador.assembleias.controller.dto.PautaDto;
import br.com.gerenciador.assembleias.controller.dto.SessaoAbertaDto;
import br.com.gerenciador.assembleias.controller.form.AbreSessaoForm;
import br.com.gerenciador.assembleias.controller.form.PautaForm;
import br.com.gerenciador.assembleias.model.Pauta;
import br.com.gerenciador.assembleias.repository.PautaRepository;
import br.com.gerenciador.assembleias.repository.VotoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/pauta")
public class PautaController {

	@Autowired
	PautaRepository pautaRepository;

	@Autowired
	VotoRepository votoRepository;

	@ApiOperation(value = "Lista todas as pautas cadastradas")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Retorna a lista de Pautas") })
	@GetMapping(produces = { "application/json" })
	public List<PautaDto> listar() {
		return pautaRepository.findAll().stream().map(pauta -> PautaDto.converter(pauta)).collect(Collectors.toList());
	}

	/**
	 * Cadastra uma nova pauta. Permite t??tulos e descri????es repetidas.
	 * 
	 * @param pautaForm
	 * @return
	 */
	@ApiOperation(value = "Cadastra nova Pauta. Permite t??tulos e descri????es repetidas")
	@ApiResponses(value = { //
			@ApiResponse(code = 201, message = "Retorna a Pauta que foi cadastrada") })
	@Transactional
	@PostMapping(consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<PautaDto> cadastrar(@RequestBody @Valid PautaForm pautaForm,
			UriComponentsBuilder uriBuilder) {
		Pauta pauta = pautaRepository.save(new Pauta(pautaForm));

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(pauta.getId()).toUri();
		return ResponseEntity.created(uri).body(PautaDto.converter(pauta));
	}

	/**
	 * Abre a sess??o da Pauta. Caso j?? est?? aberta ou n??o encontra a Pauta, retorna
	 * erro.
	 * 
	 * @param id             da pauta
	 * @param abreSessaoForm ?? opcinal, por??m se informado, precisa informar todos
	 *                       os campos, mesmo que informe zero
	 * @return
	 */
	@ApiOperation(value = "Abre uma sess??o para vota????o")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Sess??o aberta com sucesso. Retorna a pauta com a sess??o aberta."),
			@ApiResponse(code = 404, message = "Pauta n??o encontrada."),
			@ApiResponse(code = 400, message = "Sess??o j?? est?? aberta.") })
	@Transactional
	@PutMapping(consumes = { "application/json" }, value = "/{id}/abrirsessao", produces = { "application/json" })
	public ResponseEntity<SessaoAbertaDto> abrirSessao(@PathVariable Long id,
			@RequestBody(required = false) @Valid AbreSessaoForm abreSessaoForm) {

		Optional<Pauta> opt = pautaRepository.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pauta pauta = opt.get();
		if (pauta.getInicioSessao() != null) {
			throw new IllegalStateException("Sess??o j?? est?? aberta.");
		}

		pauta.abreSessao(abreSessaoForm);

		return ResponseEntity.ok(SessaoAbertaDto.converter(pauta));

	}

	@ApiOperation(value = "Consulta os detalhes de uma pauta")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Retorna os detalhes da pauta."),
			@ApiResponse(code = 404, message = "Pauta n??o encontrada.") })
	@Transactional
	@GetMapping(value = "/{id}", produces = { "application/json" })
	public ResponseEntity<PautaDetalhesDto> detalhes(@PathVariable Long id) {
		Optional<Pauta> opt = pautaRepository.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(PautaDetalhesDto.converter(opt.get()));
	}
}
