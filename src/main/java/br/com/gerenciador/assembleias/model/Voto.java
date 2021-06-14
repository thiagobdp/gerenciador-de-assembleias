package br.com.gerenciador.assembleias.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.gerenciador.assembleias.controller.form.VotoForm;

@Entity
public class Voto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String cpf;

	@NotNull
	@Enumerated(EnumType.STRING)
	private VotoEnum voto;

	@NotNull
	private LocalDateTime dataHoraVoto;

	@NotNull
	@ManyToOne
	private Pauta pauta;

	public Voto(VotoForm votoForm, Pauta pauta) {
		super();
		this.cpf = votoForm.getCpf();
		this.voto = votoForm.getVoto();
		this.dataHoraVoto = LocalDateTime.now();
		this.pauta = pauta;
	}

	public Voto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public VotoEnum getVoto() {
		return voto;
	}

	public void setVoto(VotoEnum voto) {
		this.voto = voto;
	}

	public LocalDateTime getDataHoraVoto() {
		return dataHoraVoto;
	}

	public void setDataHoraVoto(LocalDateTime dataHoraVoto) {
		this.dataHoraVoto = dataHoraVoto;
	}

	public Pauta getPauta() {
		return pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}

	public static Voto votar(@Valid VotoForm votoForm, Pauta pauta) {
		if (pauta.getVotos().stream().filter(voto -> voto.getCpf().compareTo(votoForm.getCpf()) == 0).count() != 0) {
			return null;
		} else {
			return new Voto(votoForm, pauta);
		}

	}

}
