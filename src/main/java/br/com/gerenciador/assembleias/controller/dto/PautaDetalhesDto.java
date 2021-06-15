package br.com.gerenciador.assembleias.controller.dto;

import java.time.LocalDateTime;

import br.com.gerenciador.assembleias.model.Pauta;
import io.swagger.annotations.ApiModelProperty;

public class PautaDetalhesDto {

	@ApiModelProperty(value = "Identificação da Pauta no banco de dados")
	private Long id;

	@ApiModelProperty(value = "Título da Pauta")
	private String titulo;

	@ApiModelProperty(value = "Descrição da Pauta")
	private String descricao;

	@ApiModelProperty(value = "Hora em que a sessão foi iniciada")
	private LocalDateTime inicioSessao;

	@ApiModelProperty(value = "Hora em que a sessão será fechada")
	private LocalDateTime fimSessao;

	@ApiModelProperty(value = "Quantidade de votos SIM")
	private Integer qtdVotosSim;

	@ApiModelProperty(value = "Quantidade de votos NÃO")
	private Integer qtdVotosNao;

	@ApiModelProperty(value = "Identifica se a sessão já está fechada")
	private Boolean sessaoFechada;

	public static PautaDetalhesDto converter(Pauta pauta) {
		PautaDetalhesDto dto = new PautaDetalhesDto();
		dto.setId(pauta.getId());
		dto.setTitulo(pauta.getTitulo());
		dto.setDescricao(pauta.getDescricao());
		dto.setDescricao(pauta.getDescricao());
		dto.setInicioSessao(pauta.getInicioSessao());
		dto.setFimSessao(pauta.getFimSessao());
		dto.setQtdVotosSim(pauta.getQtdVotosSim());
		dto.setQtdVotosNao(pauta.getQtdVotosNao());
		dto.setSessaoFechada(pauta.getSessaoFechada());
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

	public LocalDateTime getInicioSessao() {
		return inicioSessao;
	}

	public void setInicioSessao(LocalDateTime inicioSessao) {
		this.inicioSessao = inicioSessao;
	}

	public LocalDateTime getFimSessao() {
		return fimSessao;
	}

	public void setFimSessao(LocalDateTime fimSessao) {
		this.fimSessao = fimSessao;
	}

	public Integer getQtdVotosSim() {
		return qtdVotosSim;
	}

	public void setQtdVotosSim(Integer qtdVotosSim) {
		this.qtdVotosSim = qtdVotosSim;
	}

	public Integer getQtdVotosNao() {
		return qtdVotosNao;
	}

	public void setQtdVotosNao(Integer qtdVotosNao) {
		this.qtdVotosNao = qtdVotosNao;
	}

	public Boolean getSessaoFechada() {
		return sessaoFechada;
	}

	public void setSessaoFechada(Boolean sessaoFechada) {
		this.sessaoFechada = sessaoFechada;
	}

}
