package com.ponto.ideal.solucoes.cuida_de_mim.model;

public class Medicao {

    private String keymed;
    private String presmax;
    private String presmim;
    private String glicemia;
    private String jejum;
    private long timestamp;

    public Medicao() {

    }

    public String getKeymed() {
        return keymed;
    }

    public void setKeymed(String keymed) {
        this.keymed = keymed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPresmax() {
        return presmax;
    }

    public String getJejum() {
        return jejum;
    }

    public void setJejum(String jejum) {
        this.jejum = jejum;
    }

    public void setPresmax(String presmax) {
        this.presmax = presmax;
    }

    public String getPresmim() {
        return presmim;
    }

    public void setPresmim(String presmim) {
        this.presmim = presmim;
    }

    public String getGlicemia() {
        return glicemia;
    }

    public void setGlicemia(String glicemia) {
        this.glicemia = glicemia;
    }

}
