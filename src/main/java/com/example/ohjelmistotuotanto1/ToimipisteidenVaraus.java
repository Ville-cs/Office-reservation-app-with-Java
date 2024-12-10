package com.example.ohjelmistotuotanto1;

import java.sql.*;
import java.lang.*;

public class ToimipisteidenVaraus {
    private int varaus_id;
    private int asiakas_id;
    private int toimipiste_id;
    private int palvelu_id;
    private String varattu_pvm;
    private String vahvistus_pvm;
    private String varattu_alkupvm;
    private String varattu_loppupvm;

    public ToimipisteidenVaraus (){}

    public int getVaraus_id() {
        return varaus_id;
    }
    public void setVaraus_id(int varaus_id) {
        this.varaus_id = varaus_id;
    }
    public int getAsiakas_id() {
        return asiakas_id;
    }
    public void setAsiakas_id(int asiakas_id) {
        this.asiakas_id = asiakas_id;
    }
    public int getToimipiste_id() {
        return toimipiste_id;
    }
    public void setToimipiste_id(int toimipiste_id) {
        this.toimipiste_id = toimipiste_id;
    }
    public String getVarattu_pvm() {
        return varattu_pvm;
    }
    public void setVarattu_pvm(String varattu_pvm) {
        this.varattu_pvm = varattu_pvm;
    }
    public String getVahvistus_pvm() {
        return vahvistus_pvm;
    }
    public void setVahvistus_pvm(String vahvistus_pvm) {
        this.vahvistus_pvm = vahvistus_pvm;
    }
    public String getVarattu_alkupvm() {
        return varattu_alkupvm;
    }
    public void setVarattu_alkupvm(String varattu_alkupvm) {
        this.varattu_alkupvm = varattu_alkupvm;
    }
    public String getVarattu_loppupvm() {
        return varattu_loppupvm;
    }
    public void setVarattu_loppupvm(String varattu_loppupvm) {
        this.varattu_loppupvm = varattu_loppupvm;
    }


    public int getPalvelu_id() {
        return palvelu_id;
    }
    public void setPalvelu_id(int palvelu_id) {
        this.palvelu_id = palvelu_id;
    }

    public void haeVaraus (Connection yhteys) throws SQLException, Exception{
        ResultSet tiedot;
        Statement sanoma = yhteys.createStatement();

        tiedot = sanoma.executeQuery("SELECT * FROM varaus WHERE varaus_id = " + getVaraus_id());
        if (!tiedot.next()) {
            throw new Exception("Varausta ei löydy");
        }
        setVaraus_id(tiedot.getInt(1));
        setAsiakas_id(tiedot.getInt(2));
        setToimipiste_id(tiedot.getInt(3));
        setVarattu_pvm(tiedot.getString(4));
        setVahvistus_pvm(tiedot.getString(5));
        setVarattu_alkupvm(tiedot.getString(6));
        setVarattu_loppupvm(tiedot.getString(7));
    }

    public void lisaaVaraus (Connection yhteys) throws SQLException, Exception {
        String arvot;
        ResultSet tiedot;

        Statement sanoma = yhteys.createStatement();
        tiedot = sanoma.executeQuery("SELECT varaus_id FROM varaus WHERE varaus_id = " + getVaraus_id());
        if (tiedot.next()) {
            throw new Exception("Varaus on jo olemassa");
        }
        arvot = "VALUES (" + getVaraus_id() + ", '" + getAsiakas_id() + "', '" + getToimipiste_id() + "', '" + getVarattu_pvm() + "', '" + getVahvistus_pvm() + "', '" + getVarattu_alkupvm() + "', '" + getVarattu_loppupvm() + "')";
        sanoma.executeUpdate("INSERT INTO  varaus " + arvot);
    }

    public void muokkaaVarausta (Connection yhteys) throws SQLException, Exception {
        String arvot;
        ResultSet tiedot;
        Statement sanoma = yhteys.createStatement();

        tiedot = sanoma.executeQuery("SELECT varaus_id FROM varaus WHERE varaus_id = " + getVaraus_id());
        if (!tiedot.next()) {
            throw new Exception("Varaus ei ole tietokannassa");
        }
        if (getAsiakas_id() != 0 && getToimipiste_id() != 0 && getVarattu_pvm() != null && getVahvistus_pvm() != null && getVarattu_alkupvm() != null) {
            arvot = "SET asiakas_id = '" + getAsiakas_id() + "', toimipiste_id = '" + getToimipiste_id() + "', varattu_pvm = '" + getVarattu_pvm() + "', vahvistus_pvm = '"
                    + getVahvistus_pvm() + "', varattu_alkupvm = '" + getVarattu_alkupvm() + "', varattu_loppupvm = '" + getVarattu_loppupvm() + "'";
            sanoma.executeUpdate("UPDATE varaus " + arvot + " WHERE varaus_id = " + getVaraus_id());
        }
        else
            System.out.println("Sijoita tiedot kaikkiin kohtiin");
    }

    public void poistaVaraus (Connection yhteys) throws SQLException, Exception {
        ResultSet tiedot;
        Statement sanoma = yhteys.createStatement();

        tiedot = sanoma.executeQuery("SELECT varaus_id FROM varaus WHERE varaus_id = " + getVaraus_id());
        if (!tiedot.next()) {
            throw new Exception("Varausta ei voida poistaa sillä sitä ei ole tietokannassa");
        }
        else
            sanoma.executeUpdate("DELETE FROM varaus WHERE varaus_id = " + getVaraus_id());
    }
}


