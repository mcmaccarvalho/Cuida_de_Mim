package com.ponto.ideal.solucoes.cuida_de_mim.model;

public class Usuario {


    private String keyusu;
    private String nomeusu;
    private String apelidousu;
    private String emailusu;
    private String imagemusuario;
    private long timestamp;
    private String token;
    private int online;

    public Usuario(){

    }

    public String getKeyusu() {
        return keyusu;
    }

    public void setKeyusu(String keyusu) {
        this.keyusu = keyusu;
    }

    public String getNomeusu() {
        return nomeusu;
    }

    public void setNomeusu(String nomeusu) {
        this.nomeusu = nomeusu;
    }

    public String getApelidousu() {
        return apelidousu;
    }

    public void setApelidousu(String apelidousu) {
        this.apelidousu = apelidousu;
    }

    public String getEmailusu() {
        return emailusu;
    }

    public void setEmailusu(String emailusu) {
        this.emailusu = emailusu;
    }

    public String getImagemusuario() {
        return imagemusuario;
    }

    public void setImagemusuario(String imagemusuario) {
        this.imagemusuario = imagemusuario;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
