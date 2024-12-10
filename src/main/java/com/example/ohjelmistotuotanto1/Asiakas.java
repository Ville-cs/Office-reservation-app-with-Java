package com.example.ohjelmistotuotanto1;

import java.sql.*;
import java.lang.*;
public class Asiakas {

    private int m_asiakas_id;
    private String m_etunimi;
    private String m_sukunimi;
    private String m_lahiosoite;
    private String m_postitoimipaikka;
    private String m_postinro;
    private String m_email;
    private String m_puhelinnro;

    public Asiakas(){

    }


    public int getAsiakasId() {
        return m_asiakas_id;
    }

    public void setAsiakasId(int m_asiakas_id) {
        this.m_asiakas_id = m_asiakas_id;
    }

    public String getEtunimi() {
        return m_etunimi;
    }

    public void setEtunimi(String m_etunimi) {
        this.m_etunimi = m_etunimi;
    }

    public String getSukunimi() {
        return m_sukunimi;
    }

    public void setSukunimi(String m_sukunimi) {
        this.m_sukunimi = m_sukunimi;
    }

    public String getLahiosoite() {
        return m_lahiosoite;
    }

    public void setLahiosoite(String m_lahiosoite) {
        this.m_lahiosoite = m_lahiosoite;
    }

    public String getPostitoimipaikka() {
        return m_postitoimipaikka;
    }

    public void setPostitoimipaikka(String m_postitoimipaikka) {
        this.m_postitoimipaikka = m_postitoimipaikka;
    }

    public String getPostinro() {
        return m_postinro;
    }

    public void setPostinro(String m_postinro) {
        this.m_postinro = m_postinro;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String m_email) {
        this.m_email = m_email;
    }

    public String getPuhelinnro() {
        return m_puhelinnro;
    }

    public void setPuhelinnro(String m_puhelinnro) {
        this.m_puhelinnro = m_puhelinnro;
    }


    public static Asiakas haeAsiakas (Connection yhteys, int id) throws SQLException, Exception {
        ResultSet tulosjoukko = null;
        try {
            PreparedStatement kasky = yhteys.prepareStatement("SELECT asiakas_id, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro "
                    + " FROM asiakas WHERE asiakas_id = ?");
            kasky.setInt( 1, id);
            tulosjoukko = kasky.executeQuery();
            if (tulosjoukko == null) {
                throw new Exception("Asiakasta ei loydy");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        Asiakas asiakas = new Asiakas ();

        try {
            while (tulosjoukko.next()){
                asiakas.setAsiakasId (tulosjoukko.getInt("asiakas_id"));
                asiakas.setEtunimi (tulosjoukko.getString("etunimi"));
                asiakas.setSukunimi(tulosjoukko.getString("sukunimi"));
                asiakas.setLahiosoite (tulosjoukko.getString("lahiosoite"));
                asiakas.setPostitoimipaikka (tulosjoukko.getString("postitoimipaikka"));
                asiakas.setPostinro (tulosjoukko.getString("postinro"));
                asiakas.setEmail (tulosjoukko.getString("email"));
                asiakas.setPuhelinnro (tulosjoukko.getString("puhelinnro"));
            }

        }catch (SQLException se) {
            throw se;
        }

        return asiakas;
    }



    public int lisaaAsiakas (Connection yhteys) throws SQLException, Exception {

        PreparedStatement kasky;
        ResultSet tulosjoukko = null;
        try {
            kasky = yhteys.prepareStatement("SELECT asiakas_id"
                    + " FROM asiakas WHERE asiakas_id = ?");
            kasky.setInt( 1, getAsiakasId());
            tulosjoukko = kasky.executeQuery();
            if (tulosjoukko.next()) {
                throw new Exception("Asiakas on jo olemassa");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        try {
            kasky = yhteys.prepareStatement("INSERT INTO asiakas "
                    + "(asiakas_id, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro) "
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            kasky.setInt( 1, getAsiakasId());
            kasky.setString(2, getEtunimi());
            kasky.setString(3, getSukunimi());
            kasky.setString(4, getLahiosoite());
            kasky.setString(5, getPostitoimipaikka ());
            kasky.setString(6, getPostinro ());
            kasky.setString(7, getEmail ());
            kasky.setString(8, getPuhelinnro ());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Asiakkaan lisaaminen ei onnistu");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }



    public int muutaAsiakas (Connection yhteys) throws SQLException, Exception {
        PreparedStatement kasky;
        ResultSet tulosjoukko = null;
        try {
            kasky = yhteys.prepareStatement("SELECT asiakas_id"
                    + " FROM asiakas WHERE asiakas_id = ?");
            kasky.setInt( 1, getAsiakasId());
            tulosjoukko = kasky.executeQuery();
            if (!tulosjoukko.next()) {
                throw new Exception("Asiakasta ei loydy tietokannasta");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        try {
            kasky = yhteys.prepareStatement("UPDATE asiakas "
                    + "SET etunimi = ?, sukunimi = ?, lahiosoite = ?, postitoimipaikka = ?, postinro = ?, email = ?, puhelinnro = ? "
                    + " WHERE asiakas_id = ?");

            kasky.setString(1, getEtunimi());
            kasky.setString(2, getSukunimi());
            kasky.setString(3, getLahiosoite());
            kasky.setString(4, getPostitoimipaikka ());
            kasky.setString(5, getPostinro ());
            kasky.setString(6, getEmail ());
            kasky.setString(7, getPuhelinnro ());
            kasky.setInt( 8, getAsiakasId());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Asiakkaan muuttaminen ei onnistunut");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }



    public int poistaAsiakas (Connection connection) throws SQLException, Exception {

        try {
            PreparedStatement kasky = connection.prepareStatement("DELETE FROM asiakas WHERE asiakas_id = ?");
            kasky.setInt( 1, getAsiakasId());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Asiakkaan poistaminen ei onnistunut");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }
}