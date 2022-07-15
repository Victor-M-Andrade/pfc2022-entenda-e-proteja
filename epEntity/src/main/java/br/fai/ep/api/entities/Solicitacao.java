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

    public static class RESQUEST_TABLE extends TABLE {
        public static final String TABLE_NAME = "solicitacao as S";
        public static final String SHORT_TABLE_NAME = "S.";
        public static final String DEMAND_COLUMN = "demanda";
        public static final String SERVICE_TYPE_COLUMN = "tipo_servico";
        public static final String ID_CLIENT_COLUMN = "id_cliente";
    }
}