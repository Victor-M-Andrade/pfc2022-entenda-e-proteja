package br.fai.ep.api.entities;

import java.sql.Timestamp;

public class Teste extends BasePojo {
    private Timestamp dataHora;
    private int acertos;
    private long idUsuario;

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(final Timestamp dataHora) {
        this.dataHora = dataHora;
    }

    public int getAcertos() {
        return acertos;
    }

    public void setAcertos(final int acertos) {
        this.acertos = acertos;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(final long idUsuario) {
        this.idUsuario = idUsuario;
    }

}
