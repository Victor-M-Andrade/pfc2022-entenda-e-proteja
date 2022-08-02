package br.fai.ep.api.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestaoTeste extends BasePojo {
    private String escolha;
    private long idQuestao;
    private long idTeste;

    public static class QUESTION_TEST_TABLE extends TABLE {
        public static final String TABLE_NAME = "teste_questao as TQ";
        public static final String SHORT_TABLE_NAME = "TQ.";
        public static final String CHOICE_COLUMN = "escolha";
        public static final String ID_QUEST_COLUMN = "id_questao";
        public static final String ID_TEST_COLUMN = "id_teste";
    }
}