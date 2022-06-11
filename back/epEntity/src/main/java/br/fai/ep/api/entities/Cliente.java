package br.fai.ep.api.entities;

import java.sql.Timestamp;

public class Cliente extends BasePojo {
	private String nome;
	private String cnpj;
	private boolean aceite;
	private Timestamp dataHora;

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(final String cnpj) {
		this.cnpj = cnpj;
	}

	public boolean isAceite() {
		return aceite;
	}

	public void setAceite(final boolean aceite) {
		this.aceite = aceite;
	}

	public Timestamp getDataHora() {
		return dataHora;
	}

	public void setDataHora(final Timestamp dataHora) {
		this.dataHora = dataHora;
	}

}
