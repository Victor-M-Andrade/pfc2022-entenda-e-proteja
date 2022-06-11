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
}
