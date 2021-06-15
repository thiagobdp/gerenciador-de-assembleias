package br.com.gerenciador.assembleias.controller.form;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class PautaForm {

	@ApiModelProperty(value = "Título para a Pauta")
	@NotBlank
	private String titulo;

	@ApiModelProperty(value = "Descrição para a Pauta")
	private String descricao;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
