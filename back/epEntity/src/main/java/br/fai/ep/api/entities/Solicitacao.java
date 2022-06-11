package br.fai.ep.api.entities;

public class Solicitacao extends BasePojo {
    private String demanda;
    private String tipoServico;
    private String idCliente;

    public String getDemanda() {
        return demanda;
    }

    public void setDemanda(final String demanda) {
        this.demanda = demanda;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(final String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(final String idCliente) {
        this.idCliente = idCliente;
    }

}
