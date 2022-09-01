package br.fai.ep.epEntities;

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

    public static class SITUATIONS extends TABLE {
        public static final String APPROVED = "APROVADO";
        public static final String REPROVED = "REPROVADO";
        public static final String REQUESTED = "SOLICITADO";
        public static final String EXCLUDED = "EXCLUIDO";
    }

    public static class PARTINER_TYPE extends TABLE {
        public static final String TECHNICAL = "TECNICO";
        public static final String LEGISLATIVE = "LEGISLATIVO";
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