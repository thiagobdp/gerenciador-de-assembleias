package br.com.gerenciador.assembleias.config.exceptionhandler;

public class ErroDeFormularioDto {

	public String campo;
	public String erro;

	public ErroDeFormularioDto(String campo, String erro) {
		super();
		this.campo = campo;
		this.erro = erro;
	}

	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}

}
