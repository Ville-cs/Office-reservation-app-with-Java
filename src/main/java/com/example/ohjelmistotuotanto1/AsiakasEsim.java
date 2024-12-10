package com.example.ohjelmistotuotanto1;

public class AsiakasEsim {
    private int id;
    private String nimi;
    private String email;
    private String puhelinNumero;
    private String kaupunki;
    private String lahiosoite;
    private String postinumero;

    // Constructor
    public AsiakasEsim(int id, String nimi, String email, String puhelinNumero, String kaupunki,
                       String lahiosoite, String postinumero) {
        this.id = id;
        this.nimi = nimi;
        this.email = email;
        this.puhelinNumero = puhelinNumero;
        this.kaupunki = kaupunki;
        this.lahiosoite = lahiosoite;
        this.postinumero = postinumero;
    }

    // Getter and setter for the 'id' attribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getPalveluId() {
        return id;
    }
    public void setPalveluId(int id) {
        this.id = id;
    }
    // Getters and setters for the other attributes
    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPuhelinNumero() {
        return puhelinNumero;
    }

    public void setPuhelinNumero(String puhelinNumero) {
        this.puhelinNumero = puhelinNumero;
    }

    public String getKaupunki() {
        return kaupunki;
    }

    public void setKaupunki(String kaupunki) {
        this.kaupunki = kaupunki;
    }

    public String getLahiOsoite() {
        return lahiosoite;
    }

    public void setLahiOsoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }

    public String getPostiNumero() {
        return postinumero;
    }

    public void setPostiNumero(String postinumero) {
        this.postinumero = postinumero;
    }

    // toString method to print the object as a string
    @Override
    public String toString() {
        return nimi;
    }
}
