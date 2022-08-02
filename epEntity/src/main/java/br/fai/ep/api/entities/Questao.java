package br.fai.ep.api.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Questao extends BasePojo {
    private String pergunta;
    private String alternativaA;
    private String alternativaB;
    private String alternativaC;
    private String alternativaD;
    private String resposta;
    private int nivel;

    public static class QUESTION_TABLE extends TABLE {
        public static final String TABLE_NAME = "questao as Q";
        public static final String SHORT_TABLE_NAME = "Q.";
        public static final String QUESTION_COLUMN = "pergunta";
        public static final String ALTERNATIVE_A_COLUMN = "alternativa_a";
        public static final String ALTERNATIVE_B_COLUMN = "alternativa_b";
        public static final String ALTERNATIVE_C_COLUMN = "alternativa_c";
        public static final String ALTERNATIVE_D_COLUMN = "alternativa_d";
        public static final String ANSWER_COLUMN = "resposta";
        public static final String LEVEL_COLUMN = "nivel";
    }
}