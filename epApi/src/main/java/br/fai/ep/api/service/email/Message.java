package br.fai.ep.api.service.email;

import java.util.List;

public class Message {

    private String remetente;
    private List<String> destinarios;
    private String assunto;
    private String corpo;

    public Message(String remetente, List<String> destinarios, String assunto, String corpo) {
        this.remetente = remetente;
        this.destinarios = destinarios;
        this.assunto = assunto;
        this.corpo = corpo;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public List<String> getDestinarios() {
        return destinarios;
    }

    public void setDestinarios(List<String> destinarios) {
        this.destinarios = destinarios;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    
}