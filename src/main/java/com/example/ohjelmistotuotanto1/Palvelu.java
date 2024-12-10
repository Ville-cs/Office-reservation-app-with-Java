package com.example.ohjelmistotuotanto1;

import java.sql.*;
import java.lang.*;
public class Palvelu {

    private int m_palvelu_id;
    private int m_toimipiste_id;
    private String m_nimi;
    private int m_tyyppi; // ei varmaan meillä ole?, mutta lisäsin kun on sql:ssä
    private String m_kuvaus;
    private double m_hinta;

    public Palvelu(){

    }

    public int getPalveluId() {
        return m_palvelu_id;
    }

    public void setPalveluId(int m_palvelu_id) {
        this.m_palvelu_id = m_palvelu_id;
    }

    public int getToimipisteId() {
        return m_toimipiste_id;
    }

    public void setToimipisteId(int m_toimipiste_id) {
        this.m_toimipiste_id = m_toimipiste_id;
    }

    public String getNimi() {
        return m_nimi;
    }

    public void setNimi(String m_nimi) {
        this.m_nimi = m_nimi;
    }

    public int getTyyppi() {
        return m_tyyppi;
    }

    public void setTyyppi(int m_tyyppi) {
        this.m_tyyppi = m_tyyppi;
    }

    public String getKuvaus() {
        return m_kuvaus;
    }

    public void setKuvaus(String m_kuvaus) {
        this.m_kuvaus = m_kuvaus;
    }

    public double getHinta() {
        return m_hinta;
    }

    public void setHinta(double m_hinta) {
        this.m_hinta = m_hinta;
    }

    public static Palvelu haePalvelu (Connection yhteys, int id) throws SQLException, Exception {
        ResultSet tulosjoukko = null;
        try {
            PreparedStatement kasky = yhteys.prepareStatement("SELECT palvelu_id, toimipiste_id, nimi, tyyppi, kuvaus, hinta "
                    + " FROM Palvelu WHERE palvelu_id = ?");
            kasky.setInt( 1, id);
            tulosjoukko = kasky.executeQuery();
            if (tulosjoukko == null) {
                throw new Exception("Palvelua ei loydy");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        Palvelu palvelu = new Palvelu ();

        try {
            while (tulosjoukko.next()){
                palvelu.setPalveluId(tulosjoukko.getInt("palvelu_id"));
                palvelu.setToimipisteId(tulosjoukko.getInt("toimipiste_id"));
                palvelu.setNimi(tulosjoukko.getString("nimi"));
                palvelu.setTyyppi(tulosjoukko.getInt("tyyppi"));
                palvelu.setKuvaus(tulosjoukko.getString("kuvaus"));
                palvelu.setHinta(tulosjoukko.getDouble("hinta"));
            }

        }catch (SQLException se) {
            throw se;
        }

        return palvelu;
    }



    public int lisaaPalvelu (Connection yhteys) throws SQLException, Exception {

        PreparedStatement kasky;
        ResultSet tulosjoukko = null;
        try {
            kasky = yhteys.prepareStatement("SELECT palvelu_id"
                    + " FROM Palvelu WHERE palvelu_id = ?");
            kasky.setInt( 1, getPalveluId());
            tulosjoukko = kasky.executeQuery();
            if (tulosjoukko.next()) {
                throw new Exception("Palvelu on jo olemassa");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        try {
            kasky = yhteys.prepareStatement("INSERT INTO Palvelu "
                    + "(palvelu_id, toimipiste_id, nimi, tyyppi, kuvaus, hinta) "
                    + " VALUES (?, ?, ?, ?, ?, ?)");
            kasky.setInt(1, getPalveluId());
            kasky.setInt(2, getToimipisteId());
            kasky.setString(3, getNimi());
            kasky.setInt(4, getTyyppi());
            kasky.setString(5, getKuvaus());
            kasky.setDouble(6, getHinta());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Palvelun lisaaminen ei onnistu");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }



    public int muutaPalvelu (Connection yhteys) throws SQLException, Exception {
        PreparedStatement kasky;
        ResultSet tulosjoukko = null;
        try {
            kasky = yhteys.prepareStatement("SELECT palvelu_id"
                    + " FROM Palvelu WHERE palvelu_id = ?");
            kasky.setInt( 1, getPalveluId());
            tulosjoukko = kasky.executeQuery();
            if (!tulosjoukko.next()) {
                throw new Exception("Palvelua ei loydy tietokannasta");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        try {
            kasky = yhteys.prepareStatement("UPDATE Palvelu "
                    + "SET toimipiste_id = ?, nimi = ? tyyppi = ?, kuvaus = ?, hinta = ? "
                    + " WHERE palvelu_id = ?");

            kasky.setInt(1, getToimipisteId());
            kasky.setString(2, getNimi());
            kasky.setInt(3, getTyyppi());
            kasky.setString(4, getKuvaus());
            kasky.setDouble(5, getHinta());
            kasky.setInt(6, getPalveluId());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Laskun muuttaminen ei onnistunut");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }



    public int poistaPalvelu (Connection connection) throws SQLException, Exception {

        try {
            PreparedStatement kasky = connection.prepareStatement("DELETE FROM Palvelu WHERE palvelu_id = ?");
            kasky.setInt( 1, getPalveluId());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Palvelun poistaminen ei onnistunut");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }
}