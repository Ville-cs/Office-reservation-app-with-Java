package com.example.ohjelmistotuotanto1;

import java.sql.*;
import java.lang.*;

public class Toimipiste {
    private int toimipiste_id;
    private String nimi;
    private String lahiosoite;
    private String postitoimipaikka;
    private String postinro;
    private String email;
    private String puhelinnro;

    public Toimipiste (){}

    public int getToimipiste_id() {
        return toimipiste_id;
    }
    public void setToimipiste_id(int toimipiste_id) {
        this.toimipiste_id = toimipiste_id;
    }
    public String getNimi() {
        return nimi;
    }
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    public String getLahiosoite() {
        return lahiosoite;
    }
    public void setLahiosoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }
    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }
    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }
    public String getPostinro() {
        return postinro;
    }
    public void setPostinro(String postinro) {
        this.postinro = postinro;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPuhelinnro() {
        return puhelinnro;
    }
    public void setPuhelinnro(String puhelinnro) {
        this.puhelinnro = puhelinnro;
    }

    public void haeToimipiste (Connection yhteys) throws SQLException, Exception{
        ResultSet tiedot;
        Statement sanoma = yhteys.createStatement();

        tiedot = sanoma.executeQuery("SELECT * FROM toimipiste WHERE toimipiste_id = " + getToimipiste_id());
        if (!tiedot.next()) {
            throw new Exception("Toimipistettä ei löydy");
        }
        setToimipiste_id(tiedot.getInt(1));
        setNimi(tiedot.getString(2));
        setLahiosoite(tiedot.getString(3));
        setPostitoimipaikka(tiedot.getString(4));
        setPostinro(tiedot.getString(5));
        setEmail(tiedot.getString(6));
        setPuhelinnro(tiedot.getString(7));
    }
    public void lisaaToimipiste (Connection yhteys) throws SQLException, Exception {
        String arvot;
        ResultSet tiedot;

        Statement sanoma = yhteys.createStatement();
        tiedot = sanoma.executeQuery("SELECT toimipiste_id FROM toimipiste WHERE toimipiste_id = " + getToimipiste_id());
        if (tiedot.next()) {
            throw new Exception("Toimipiste on jo olemassa");
        }
        arvot = "VALUES (" + getToimipiste_id() + ", '" + getNimi() + "', '" + getLahiosoite() + "', '" + getPostitoimipaikka() + "', '" + getPostinro() + "', '" + getEmail() + "', '" + getPuhelinnro() +"')";
        sanoma.executeUpdate("INSERT INTO  toimipiste " + arvot);
    }
    public void muokkaaToimipiste (Connection yhteys) throws SQLException, Exception {
        String arvot;
        ResultSet tiedot;
        Statement sanoma = yhteys.createStatement();

        tiedot = sanoma.executeQuery("SELECT toimipiste_id FROM toimipiste WHERE toimipiste_id = " + getToimipiste_id());
        if (!tiedot.next()) {
            throw new Exception("Toimipiste ei ole tietokannassa");
        }
        if (getNimi() != null && getLahiosoite() != null && getPostitoimipaikka() != null && getPostinro() != null && getEmail() != null && getPuhelinnro() != null) {
            arvot = "SET nimi = '" + getNimi() + "', lahiosoite = '" + getLahiosoite() + "', postitoimipaikka = '" + getPostitoimipaikka() + "', postinro = '" + getPostinro() + "', email = '" + getEmail() + "', puhelinnro = '" + getPuhelinnro() + "'";
            sanoma.executeUpdate("UPDATE toimipiste " + arvot + " WHERE toimipiste_id = " + getToimipiste_id());
        }
        else
            System.out.println("Sijoita tiedot kaikkiin kohtiin");
    }
    public void poistaToimipiste (Connection yhteys) throws SQLException, Exception {
        ResultSet tiedot;
        Statement sanoma = yhteys.createStatement();

        tiedot = sanoma.executeQuery("SELECT toimipiste_id FROM toimipiste WHERE toimipiste_id = " + getToimipiste_id());
        if (!tiedot.next()) {
            throw new Exception("Toimipistettä ei voida poistaa sillä sitä ei ole tietokannassa");
        }
        else
            sanoma.executeUpdate("DELETE FROM toimipiste WHERE toimipiste_id = " + getToimipiste_id());
    }
}

