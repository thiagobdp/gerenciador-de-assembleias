package br.com.gerenciador.assembleias.controller.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class AbreSessaoForm {

	@ApiModelProperty(value = "Quantidade de horas para duração da sessão")
	@NotNull
	private Long duracaoEmHoras;

	@ApiModelProperty(value = "Quantidade de minutos para duração da sessão")
	@NotNull
	private Long duracaoEmMinutos;

	public Long getDuracaoEmHoras() {
		return duracaoEmHoras;
	}

	public void setDuracaoEmHoras(Long duracaoEmHoras) {
		this.duracaoEmHoras = duracaoEmHoras;
	}

	public Long getDuracaoEmMinutos() {
		return duracaoEmMinutos;
	}

	public void setDuracaoEmMinutos(Long duracaoEmMinutos) {
		this.duracaoEmMinutos = duracaoEmMinutos;
	}

}
