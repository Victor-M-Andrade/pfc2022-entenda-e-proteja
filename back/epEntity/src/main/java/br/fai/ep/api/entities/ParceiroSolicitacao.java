package br.fai.ep.api.entities;

public class ParceiroSolicitacao extends BasePojo {
    private long idParceiro;
    private long idSolicitacao;

    public long getIdParceiro() {
        return idParceiro;
    }

    public void setIdParceiro(final long idParceiro) {
        this.idParceiro = idParceiro;
    }

    public long getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(final long idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }
}
