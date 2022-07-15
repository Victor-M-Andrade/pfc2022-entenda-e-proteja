package br.fai.ep.api.entities;

public class Questao extends BasePojo {

    private String pergunta;
    private String alternativaA;
    private String alternativaB;
    private String alternativaC;
    private String alternativaD;
    private String resposta;
    private int nivel;

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(final String pergunta) {
        this.pergunta = pergunta;
    }

    public String getAlternativaA() {
        return alternativaA;
    }

    public void setAlternativaA(final String alternativaA) {
        this.alternativaA = alternativaA;
    }

    public String getAlternativaB() {
        return alternativaB;
    }

    public void setAlternativaB(final String alternativaB) {
        this.alternativaB = alternativaB;
    }

    public String getAlternativaC() {
        return alternativaC;
    }

    public void setAlternativaC(final String alternativaC) {
        this.alternativaC = alternativaC;
    }

    public String getAlternativaD() {
        return alternativaD;
    }

    public void setAlternativaD(final String alternativaD) {
        this.alternativaD = alternativaD;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(final String resposta) {
        this.resposta = resposta;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(final int nivel) {
        this.nivel = nivel;
    }

}
