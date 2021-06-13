package br.com.gerenciador.assembleias.controller.dto;

import br.com.gerenciador.assembleias.model.Pauta;
import io.swagger.annotations.ApiModelProperty;

public class PautaDto {

	@ApiModelProperty(value="Identificação da Pauta no banco de dados")
	private Long id;

	@ApiModelProperty(value="Título da Pauta")
	private String titulo;

	@ApiModelProperty(value="Descrição da Pauta")
	private String descricao;

	public static PautaDto converter(Pauta pauta) {
		PautaDto dto = new PautaDto();
		dto.setDescricao(pauta.getDescricao());
		dto.setId(pauta.getId());
		dto.setTitulo(pauta.getTitulo());
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
