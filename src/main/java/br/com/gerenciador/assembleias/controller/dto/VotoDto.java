package br.com.gerenciador.assembleias.controller.dto;

import java.time.LocalDateTime;

import br.com.gerenciador.assembleias.model.Voto;
import br.com.gerenciador.assembleias.model.VotoEnum;
import io.swagger.annotations.ApiModelProperty;

public class VotoDto {

	@ApiModelProperty(value="Identificação do Voto no banco de dados")
	private Long id;
	
	@ApiModelProperty(value="CPF do usuário que realizou o voto")
	private String cpf;
	
	@ApiModelProperty(value="Voto do usuário")
	private VotoEnum voto;
	
	@ApiModelProperty(value="Hora em que o voto foi realizado")
	private LocalDateTime dataHoraVoto;
	
	@ApiModelProperty(value="Identificação da Pauta no banco de dados")
	private Long pautaId;

	public VotoDto(Voto voto) {
		this.id = voto.getId();
		this.cpf = voto.getCpf();
		this.voto = voto.getVoto();
		this.dataHoraVoto = voto.getDataHoraVoto();
		this.pautaId = voto.getPauta().getId();
	}

	public VotoDto() {
		super();
	}

	public static VotoDto converter(Voto voto) {
		return new VotoDto(voto);
	}

	public Long getId() {
		return id;
	}

	public String getCpf() {
		return cpf;
	}

	public VotoEnum getVoto() {
		return voto;
	}

	public LocalDateTime getDataHoraVoto() {
		return dataHoraVoto;
	}

	public Long getPautaId() {
		return pautaId;
	}

}
