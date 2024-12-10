package com.example.ohjelmistotuotanto1;
import java.sql.*;

public class Raportointi {
    private String alku_pvm;
    private String loppu_pvm;

    public Raportointi(){}

    public String getAlku_pvm() {
        return alku_pvm;
    }
    public void setAlku_pvm(String alku_pvm) {
        this.alku_pvm = alku_pvm;
    }
    public String getLoppu_pvm() {
        return loppu_pvm;
    }
    public void setLoppu_pvm(String loppu_pvm) {
        this.loppu_pvm = loppu_pvm;
    }

    public void toimipisteidenRaportti (Connection yhteys) throws SQLException, Exception {
        ResultSet tiedot;
        Statement sanoma = yhteys.createStatement();

        tiedot = sanoma.executeQuery("SELECT varaus_id, asiakas_id, toimipiste_id, varattu_alkupvm, varattu_loppupvm FROM varaus WHERE DATE(varattu_alkupvm) BETWEEN '" + getAlku_pvm() + "' AND '" + getLoppu_pvm() + "';");
        if (!tiedot.next())
            throw new Exception("Ei varauksia annetulla aikavälillä");
        while (tiedot.next()) {
            System.out.println("varaus_id: " + tiedot.getString(1) + " asiakas_id: " + tiedot.getString(2) + " toimipiste_id: " + tiedot.getString(3) +
                    " varaus alkoi: " + tiedot.getString(4) + " ja loppui: " + tiedot.getString(5));
        }
    }
    public void palveluidenRaportti (Connection yhteys) throws SQLException, Exception {
        ResultSet tiedot;
        Statement sanoma = yhteys.createStatement();

        tiedot = sanoma.executeQuery("SELECT vp.palvelu_id, nimi, vp.varaus_id, v.toimipiste_id, varattu_alkupvm, varattu_loppupvm FROM varauksen_palvelut AS vp, varaus AS v, palvelu AS p " +
                "WHERE vp.varaus_id = v.varaus_id AND vp.palvelu_id = p.palvelu_id AND DATE(varattu_alkupvm) BETWEEN '" + getAlku_pvm() + "' AND '" + getLoppu_pvm() + "';");
        if (!tiedot.next())
            throw new Exception("Ei varauksia annetulla aikavälillä");
        while (tiedot.next()) {
            System.out.println("palvelu_id: " + tiedot.getString(1) + ", palvelun nimi: " + tiedot.getString(2) + ", varaus_id: " + tiedot.getString(3) +
                    ", toimipiste_id: " + tiedot.getString(4) + ", varaus alkoi: " + tiedot.getString(5) + " ja loppui: " + tiedot.getString(6));
        }
    }
}