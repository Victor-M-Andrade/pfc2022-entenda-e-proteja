package br.fai.ep.api.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parceiro extends Usuario {
    private String cnpj;
    private String website;
    private String situacao;
    private String descricao;
    private String tipoServico;
    private String nomeEmpresa;
    private long idUsuario;

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


    public static class PARTINER_TABLE extends TABLE {
        public static final String TABLE_NAME = "parceiro as P";
        public static final String SHORT_TABLE_NAME = "P.";
        public static final String CNPJ_COLUMN = "cnpj";
        public static final String WEBSITE_COLUMN = "site_parceiro";
        public static final String SITUATION_COLUMN = "situacao";
        public static final String DESCRIPTION_COLUMN = "descricao";
        public static final String SERVICE_TYPE_COLUMN = "tipo_servico";
        public static final String COMPANY_NAME_COLUMN = "nome_empresa";
        public static final String ID_USER_COLUMN = "id_usuario";
    }
}