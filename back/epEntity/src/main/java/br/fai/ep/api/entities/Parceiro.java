package br.fai.ep.api.entities;

public class Parceiro extends Usuario {
    private String cnpj;
    private String website;
    private String situacao;
    private String descricao;
    private String tipoServico;
    private String nomeEmpresa;
    private long idUsuario;

    public String getCnpj() {
        return this.cnpj;
    }

    public void setCnpj(final String cnpj) {
        this.cnpj = cnpj;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public String getSituacao() {
        return this.situacao;
    }

    public void setSituacao(final String situacao) {
        this.situacao = situacao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(final String descricao) {
        this.descricao = descricao;
    }

    public String getTipoServico() {
        return this.tipoServico;
    }

    public void setTipoServico(final String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getNomeEmpresa() {
        return this.nomeEmpresa;
    }

    public void setNomeEmpresa(final String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public long getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(final long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public static enum PartinerSituation {
        APPROVED("APROVADO"),
        REPROVED("REPROVADO"),
        REQUESTED("SOLICITADO"),
        EXCLUDED("EXCLUIDO");

        private final String name;

        private PartinerSituation(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum PartinerType {
        TECHNICAL("TECNICO"),
        LEGISLATIVE("LEGISLATIVO");

        private final String name;

        private PartinerType(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public String getNameByIndex(final int index) {
        if (index > PartinerSituation.values().length) {
            return "Tipo inválido";
        }

        if (PartinerSituation.values().length == index) {
            String names = "";
            for (final PartinerSituation situation : PartinerSituation.values()) {
                names += situation.name + ";";
            }
            return names;
        }

        for (final PartinerSituation situation : PartinerSituation.values()) {
            if (situation.ordinal() == index) {
                return situation.name;
            }
        }

        return "";
    }
}
