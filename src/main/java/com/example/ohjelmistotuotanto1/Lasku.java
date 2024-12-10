package com.example.ohjelmistotuotanto1;

public class Lasku {
    private int id;
    private String nimi;
    private String sahkoposti;
    private String puhelinnumero;
    private String pvaLasku;
    private String eraLasku;
    private String kuvausLasku;
    private String laskunTila;
    private double summa;
    private double alv;
    private int asiakasId;
    private String asiakkaanNimi;
    private String asiakkaanEmail;
    private String asiakkaanPuhelinnumero;

    public Lasku(int id, String nimi, String sahkoposti, String puhelinnumero, double summa, double alv,
                 String pvaLasku, String eraLasku, String kuvausLasku, String laskunTila) {
        this.id = id;
        this.nimi = nimi;
        this.sahkoposti = sahkoposti;
        this.puhelinnumero = puhelinnumero;
        this.summa = summa;
        this.alv = alv;
        this.pvaLasku = pvaLasku;
        this.eraLasku = eraLasku;
        this.kuvausLasku = kuvausLasku;
        this.laskunTila = laskunTila;
    }



    public String getpvaLasku() {
        return pvaLasku;
    }

    public String getEraLasku() {
        return eraLasku;
    }

    public void setEraLasku(String eraLasku) {
        this.eraLasku = eraLasku;
    }

    public String getKuvausLasku() {
        return kuvausLasku;
    }

    public void setKuvausLasku(String kuvausLasku) {
        this.kuvausLasku = kuvausLasku;
    }

    public String getLaskunTila() {
        return laskunTila;
    }

    public void setLaskunTila(String laskunTila) {
        this.laskunTila = laskunTila;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public double getAlv() {
        return alv;
    }

    public void setAlv(double alv) {
        this.alv = alv;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public String getAsiakkaanNimi() {
        return asiakkaanNimi;
    }

    public void setAsiakkaanNimi(String asiakkaanNimi) {
        this.asiakkaanNimi = asiakkaanNimi;
    }

    public String getAsiakkaanEmail() {
        return asiakkaanEmail;
    }

    public void setAsiakkaanEmail(String asiakkaanEmail) {
        this.asiakkaanEmail = asiakkaanEmail;
    }

    public String getAsiakkaanPuhelinnumero() {
        return asiakkaanPuhelinnumero;
    }

    public void setAsiakkaanPuhelinnumero(String asiakkaanPuhelinnumero) {
        this.asiakkaanPuhelinnumero = asiakkaanPuhelinnumero;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return nimi;
    }

    public int id() {
    return id;}
}