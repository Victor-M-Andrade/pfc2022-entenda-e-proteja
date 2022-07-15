package br.fai.ep.api.entities;

public class QuestaoTeste extends BasePojo {
    private String escolha;
    private long idQuestao;
    private long idTeste;

    public String getEscolha() {
        return escolha;
    }

    public void setEscolha(final String escolha) {
        this.escolha = escolha;
    }

    public long getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(final long idQuestao) {
        this.idQuestao = idQuestao;
    }

    public long getIdTeste() {
        return idTeste;
    }

    public void setIdTeste(final long idTeste) {
        this.idTeste = idTeste;
    }

    public static class QUESTION_TEST_TABLE extends TABLE {
        public static final String TABLE_NAME = "teste_questao as TQ";
        public static final String SHORT_TABLE_NAME = "TQ.";
        public static final String CHOICE_COLUMN = "escolha";
        public static final String ID_QUEST_COLUMN = "id_questao";
        public static final String ID_TEST_COLUMN = "id_teste";
    }
}