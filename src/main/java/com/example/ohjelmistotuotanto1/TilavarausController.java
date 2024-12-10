package com.example.ohjelmistotuotanto1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import javax.xml.transform.Result;
import java.io.*;
import java.sql.*;


public class TilavarausController {

    ObservableList<PalveluEsim> palvelulista = FXCollections.observableArrayList();
    ObservableList<Lasku> laskulista = FXCollections.observableArrayList();
    @FXML
    private TextField asiakaslaskuNimi;
    @FXML
    private TextField emailLasku;
    @FXML
    private TextField puhelinroLasku;
    @FXML
    private TextField summaLasku;
    @FXML
    private TextField pvaLasku;
    @FXML
    private TextField eraLasku;
    @FXML
    private TextArea kuvausLasku;
    @FXML
    private TextField laskunTila;

    @FXML
    private TextField palveluHaku;
    @FXML
    private TextField laskuHaku;
    @FXML // asiakaslista
    private ListView<String> asiakasListView;
    @FXML
    private ListView<PalveluEsim> palveluListView;
    @FXML
    private ListView<Lasku> laskuListView;
    @FXML
    private Connection m_conn;

    @FXML
    private TextField asiakkaanEmail;

    @FXML
    private TextField asiakkaanPuh;

    @FXML
    private TextField asiakkaanKaupunki;

    @FXML
    private TextField asiakkaanPostinumero;

    @FXML
    private TextField asiakkaanOsoite;
    @FXML
    private TextField palvelunNimi;

    @FXML
    private TextField palvelunHinta;
    @FXML
    private TextArea kuvaus;
    @FXML
    private ListView<String> lvTila;
    @FXML
    private ListView<String> lvTulevatVaraukset;
    @FXML
    private TextField tfTilaHaku;
    @FXML
    private TextField tfRaporttiAlku;
    @FXML
    private TextField tfRaporttiLoppu;
    @FXML
    private TextField tfTilaToimipisteID;
    @FXML
    private TextField tfTilaKaupunki;
    @FXML
    private TextField tfTilaNimi;
    @FXML
    private TextField tfTilaOsoite;
    @FXML
    private TextField tfTilaPostinro;

    //Varausten hallintakentät
    @FXML
    private ListView<String> varausLista;
    @FXML
    private TextField haeVaraus;
    @FXML
    private TextField tfAlku;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfEtunimi;
    @FXML
    private TextField tfKaupunki;
    @FXML
    private TextField tfLoppu;
    @FXML
    private TextField tfNimi;
    @FXML
    private TextField tfOsoite;
    @FXML
    private TextField tfPostinro;
    @FXML
    private TextField tfPuhelinnro;
    @FXML
    private TextField tfSukunimi;
    @FXML
    private TextField tfToimipisteID;
    @FXML
    private TextField tfAsiakasID;
    @FXML
    private TextField tfVarausID;
    @FXML
    private ListView<String> lvAsiakkaanVaraukset;
    @FXML
    private TextField asiakkaanEtunimi;
    @FXML
    private TextField asiakkaanSukunimi;
    @FXML
    private TextField asiakkaanID;
    @FXML
    private TextField haeAsiakas;
    @FXML
    private TextField tfVarausPalvelu1;
    @FXML
    private TextField tfVarausPalvelu2;

    @FXML
    private void lisaaPalveluPressed() {
        try {
            String query = "INSERT INTO Palvelu (nimi, tyyppi, kuvaus, hinta, alv) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = m_conn.prepareStatement(query);
            statement.setString(1, palvelunNimi.getText());
            statement.setInt(2, 1); // TODO:  Tyyppi ?
            statement.setString(3, kuvaus.getText());
            statement.setDouble(4, Double.parseDouble(palvelunHinta.getText()));
            statement.setDouble(5, 0.24); // TODO:  VAT ?

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Uusi palvelu lisätty tietokantaan");

                // Luo uusi PalveluEsim-olio, joka sisältää samat tiedot kuin juuri lisätty Palvelu.
                PalveluEsim uusiPalvelu = new PalveluEsim(
                        // TODO: ID Palvelulle?
                        // TODO: Luo ikkuna palveluiden hallintaan, millä voi valita mihin tilaan se kuuluu?
                        0,
                        palvelunNimi.getText(),
                        Double.parseDouble(palvelunHinta.getText()),
                        kuvaus.getText()
                );

                // Lisää uusi PalveluEsim-olio palvelulistalle (palvelulista on observable list).
                listaaPalvelut();
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Virhe lisätessä palvelua tietokantaan");
        } catch (Exception e) {
            e.printStackTrace();
        }

        palvelunNimi.clear();
        palvelunHinta.clear();
        kuvaus.clear();
    }

    @FXML
    private void poistaPalveluPressed() {
        PalveluEsim poistettava = palveluListView.getSelectionModel().getSelectedItem();
        if (poistettava != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Vahvista poisto");
            alert.setHeaderText("Vahvista poisto");
            alert.setContentText("Oletko varma että haluat poistaa?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                try {
                    Statement lause = m_conn.createStatement();
                    lause.executeUpdate("DELETE FROM Palvelu WHERE palvelu_id = " + poistettava.id());
                    // Valmista DELETE-lauseke palvelun poistamiseksi tietokannasta
                    listaaPalvelut();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void lisaaLaskuPressed() {
        try {
            String query = "INSERT INTO Lasku (nimi, sahkoposti, puhelinnumero, summa, alv, paivalasku, erapaiva, laskukuvaus, laskutila) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = m_conn.prepareStatement(query);
            statement.setString(1, asiakaslaskuNimi.getText());
            statement.setString(2, emailLasku.getText());
            statement.setString(3, puhelinroLasku.getText());
            statement.setDouble(4, Double.parseDouble(summaLasku.getText()));
            statement.setDouble(5, 0.24); // TODO:  VAT ?
            statement.setString(6, pvaLasku.getText());
            statement.setString(7, eraLasku.getText());
            statement.setString(8, kuvausLasku.getText());
            statement.setString(9, laskunTila.getText());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Uusi lasku lisätty tietokantaan");

                // Luo uusi Lasku-olio, joka sisältää samat tiedot kuin juuri lisätty lasku.
                Lasku uusiLasku = new Lasku(

                        0,
                        asiakaslaskuNimi.getText(),
                        emailLasku.getText(),
                        puhelinroLasku.getText(),
                        Double.parseDouble(summaLasku.getText()),
                        0.24, // provide the missing 'alv' argument here
                        pvaLasku.getText(),
                        eraLasku.getText(),
                        kuvausLasku.getText(),
                        laskunTila.getText()
                );

                // Lisää uusi Lasku-olio laskulistalle (laskulista on observable list).
                listaaLaskut();
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Virhe lisätessä laskua tietokantaan");
        } catch (Exception e) {
            e.printStackTrace();
        }

        asiakaslaskuNimi.clear();
        emailLasku.clear();
        puhelinroLasku.clear();
        summaLasku.clear();
        pvaLasku.clear();
        eraLasku.clear();
        kuvausLasku.clear();
        laskunTila.clear();
    }

    @FXML
    private void poistaLaskuPressed() {
        Lasku poistettava = laskuListView.getSelectionModel().getSelectedItem();
        if (poistettava != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Vahvista poisto");
            alert.setHeaderText("Vahvista poisto");
            alert.setContentText("Oletko varma että haluat poistaa?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                try {
                    // Valmista DELETE-lauseke palvelun poistamiseksi tietokannasta
                    Statement lause = m_conn.createStatement();
                    lause.executeUpdate("DELETE FROM lasku WHERE lasku_id = " + poistettava.id());
                    listaaLaskut();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void tulostaLasku() {
        Lasku selectedLasku = laskuListView.getSelectionModel().getSelectedItem();
        if (selectedLasku != null) {
            try {
                // Kirjoittaa laskun tiedot txt filuun.
                FileWriter fileWriter = new FileWriter(selectedLasku.getNimi() + "lasku" + ".txt");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println("Nimi: " + selectedLasku.getNimi());
                printWriter.println("Sähköposti: " + selectedLasku.getSahkoposti());
                printWriter.println("Puhelinnumero: " + selectedLasku.getPuhelinnumero());
                printWriter.println("Summa: " + selectedLasku.getSumma() + "€");
                printWriter.println("ALV: " + selectedLasku.getAlv());
                printWriter.println("Päiväys: " + selectedLasku.getpvaLasku());
                printWriter.println("Eräpäivä: " + selectedLasku.getEraLasku());
                printWriter.println("Kuvaus: ");
                String[] invoiceItems = selectedLasku.getKuvausLasku().split("\n");
                for (String item : invoiceItems) {
                    printWriter.println("- " + item);
                }
                printWriter.println("Tila: " + selectedLasku.getLaskunTila());
                printWriter.close();
                System.out.println("Laskun tiedot tallennettu tiedostoon " + selectedLasku.getNimi() + "lasku" + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Virhe tallennettaessa laskun tietoja tiedostoon");
            }
        }
    }

    //Lisätään varaus
    @FXML
    void lisaaVarausPainike(ActionEvent event) throws Exception {
        Toimipiste toimipisteOlio = new Toimipiste();
        ToimipisteidenVaraus toimipisteidenVarausOlio = new ToimipisteidenVaraus();
        Asiakas asiakasOlio = new Asiakas();
        Palvelu palveluOlio = new Palvelu();

        toimipisteOlio.setPostitoimipaikka(tfKaupunki.getText());
        toimipisteOlio.setNimi(tfNimi.getText());
        toimipisteOlio.setPuhelinnro(tfPuhelinnro.getText());
        toimipisteOlio.setEmail(tfEmail.getText());
        toimipisteOlio.setLahiosoite(tfOsoite.getText());
        toimipisteOlio.setPostinro(tfPostinro.getText());
        toimipisteOlio.setToimipiste_id(Integer.parseInt(tfToimipisteID.getText()));

        toimipisteidenVarausOlio.setVarattu_alkupvm(tfAlku.getText());
        toimipisteidenVarausOlio.setVarattu_loppupvm(tfLoppu.getText());
        toimipisteidenVarausOlio.setVaraus_id(Integer.parseInt(tfVarausID.getText()));
        toimipisteidenVarausOlio.setAsiakas_id(Integer.parseInt(tfAsiakasID.getText()));
        toimipisteidenVarausOlio.setToimipiste_id(Integer.parseInt(tfToimipisteID.getText()));;

        asiakasOlio.setEtunimi(tfEtunimi.getText());
        asiakasOlio.setSukunimi(tfSukunimi.getText());


        try {
            Statement lause = m_conn.createStatement();
            lause.executeUpdate("INSERT INTO varaus (varaus_id, asiakas_id, toimipiste_id, varattu_alkupvm, varattu_loppupvm) "
                    + "VALUES (" + toimipisteidenVarausOlio.getVaraus_id() + ", " + toimipisteidenVarausOlio.getAsiakas_id() + ", " + toimipisteidenVarausOlio.getToimipiste_id() + ", '" + toimipisteidenVarausOlio.getVarattu_alkupvm() + "', '" + toimipisteidenVarausOlio.getVarattu_loppupvm() + "')");

            if (tfVarausPalvelu1.getText().equals("")) {
            } else {
                ResultSet tulokset1 = lause.executeQuery("SELECT palvelu_id FROM palvelu WHERE " +
                        "nimi = " + "'" + tfVarausPalvelu1.getText() + "'");
                tulokset1.next();
                int palvelu_id = tulokset1.getInt("palvelu_id");
                lause.executeUpdate("INSERT INTO varauksen_palvelut " +
                        "VALUES (" + toimipisteidenVarausOlio.getVaraus_id() +
                        ", " + palvelu_id + ", " + 1 + ")");
            }

            if (tfVarausPalvelu2.getText().equals("")) {
            } else {
                ResultSet tulokset2 = lause.executeQuery("SELECT palvelu_id FROM palvelu WHERE " +
                        "nimi = " + "'" + tfVarausPalvelu2.getText() + "'");
                tulokset2.next();
                int palvelu_id2 = tulokset2.getInt("palvelu_id");
                lause.executeUpdate("INSERT INTO varauksen_palvelut " +
                        "VALUES (" + toimipisteidenVarausOlio.getVaraus_id() +
                        ", " + palvelu_id2 + ", " + 1 + ")");
            }
            listaaVaraukset();
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Poistetaan varaus
    @FXML
    void poistaVarausPainike(ActionEvent event) throws Exception {
        ToimipisteidenVaraus toimipisteidenVarausOlio = new ToimipisteidenVaraus();
        toimipisteidenVarausOlio.setVaraus_id(Integer.parseInt(tfVarausID.getText()));

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vahvista poisto");
        alert.setHeaderText("Vahvista poisto");
        alert.setContentText("Oletko varma että haluat poistaa?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            try {
                Statement lause = m_conn.createStatement();
                lause.executeUpdate("DELETE FROM varaus WHERE varaus_id = " + toimipisteidenVarausOlio.getVaraus_id());
                tfAsiakasID.clear(); tfVarausID.clear(); tfToimipisteID.clear(); tfLoppu.clear(); tfAlku.clear();
                tfVarausPalvelu1.clear(); tfVarausPalvelu2.clear(); tfTilaNimi.clear(); tfTilaOsoite.clear();
                tfKaupunki.clear(); tfPostinro.clear(); tfEtunimi.clear(); tfSukunimi.clear(); tfEmail.clear(); tfPuhelinnro.clear();
                tfNimi.clear(); tfOsoite.clear();
                listaaVaraukset();
            } catch (Exception e) {
                System.err.println("Virhe");
                System.err.println(e.getMessage());
            }
        }
    }


    //Haetaan varaus
    @FXML
    void haeVarausPainike(ActionEvent event) throws Exception {
        haeVaraus();
    }

    public void haeVaraus() {
        ToimipisteidenVaraus varausOlio = new ToimipisteidenVaraus();
        if (!haeVaraus.getText().equals("")) {
            varausOlio.setVaraus_id(Integer.parseInt(haeVaraus.getText()));
            try {
                Statement lause = m_conn.createStatement();
                ResultSet tulokset = lause.executeQuery("SELECT * FROM varaus " +
                        "WHERE varaus_id = " + varausOlio.getVaraus_id());
                tulokset.next();
                int varaus_id = tulokset.getInt("varaus_id");
                int asiakas_id = tulokset.getInt("asiakas_id");
                int toimipiste_id = tulokset.getInt("toimipiste_id");
                String alku = tulokset.getString("varattu_alkupvm");
                String loppu = tulokset.getString("varattu_loppupvm");

                tfVarausID.setText(Integer.toString(varaus_id));
                tfAsiakasID.setText(Integer.toString(asiakas_id));
                tfToimipisteID.setText(Integer.toString(toimipiste_id));
                tfAlku.setText(alku);
                tfLoppu.setText(loppu);

                ResultSet tulokset2 = lause.executeQuery("SELECT * FROM asiakas " +
                        "WHERE asiakas_id = " + asiakas_id);
                tulokset2.next();
                String etunimi = tulokset2.getString("etunimi");
                String sukunimi = tulokset2.getString("sukunimi");
                String sposti = tulokset2.getString("email");
                String puhelinnro = tulokset2.getString("puhelinnro");

                tfEtunimi.setText(etunimi);
                tfSukunimi.setText(sukunimi);
                tfEmail.setText(sposti);
                tfPuhelinnro.setText(puhelinnro);

                ResultSet tulokset3 = lause.executeQuery("SELECT * FROM toimipiste " +
                        "WHERE toimipiste_id = " + toimipiste_id);
                tulokset3.next();
                String tila = tulokset3.getString("nimi");
                String lahiosoite = tulokset3.getString("lahiosoite");
                String kaupunki = tulokset3.getString("postitoimipaikka");
                String postinro = tulokset3.getString("postinro");

                tfNimi.setText(tila);
                tfOsoite.setText(lahiosoite);
                tfKaupunki.setText(kaupunki);
                tfPostinro.setText(postinro);

            } catch (Exception e) {
                System.err.println("Virhe");
                System.err.println(e.getMessage());
            }
        }



    }

    //Muutetaan varausta
    @FXML
    void muutaTietojaPainike(ActionEvent event) throws Exception {
        ToimipisteidenVaraus toimipisteidenVarausOlio = new ToimipisteidenVaraus();

        toimipisteidenVarausOlio.setVarattu_alkupvm(tfAlku.getText());
        toimipisteidenVarausOlio.setVarattu_loppupvm(tfLoppu.getText());
        toimipisteidenVarausOlio.setVaraus_id(Integer.parseInt(tfVarausID.getText()));
        toimipisteidenVarausOlio.setAsiakas_id(Integer.parseInt(tfAsiakasID.getText()));
        toimipisteidenVarausOlio.setToimipiste_id(Integer.parseInt(tfToimipisteID.getText()));

        try {
            Statement lause = m_conn.createStatement();

            lause.executeUpdate("UPDATE varaus "
                    + "SET varattu_alkupvm = '" + toimipisteidenVarausOlio.getVarattu_alkupvm()
                    + "', varattu_loppupvm = '" + toimipisteidenVarausOlio.getVarattu_loppupvm()
                    + "', asiakas_id = '" + toimipisteidenVarausOlio.getAsiakas_id()
                    + "', toimipiste_id = '" + toimipisteidenVarausOlio.getToimipiste_id()
                    + "' WHERE varaus_id = " + toimipisteidenVarausOlio.getVaraus_id());
            haeVaraus();
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Listataan varaukset
    @FXML
    void listaaVarauksetPainike(ActionEvent event) throws Exception {
        listaaVaraukset();
    }

    public void listaaVaraukset() {
        ObservableList<String> tilat = FXCollections.observableArrayList();
        try {
            Statement lause = m_conn.createStatement();
            ResultSet tulokset = lause.executeQuery("SELECT * FROM varaus");
            while (tulokset.next()) {
                int varaus_id = tulokset.getInt("varaus_id");
                int asiakas_id = tulokset.getInt("asiakas_id");
                int toimipiste_id = tulokset.getInt("toimipiste_id");
                String alku = tulokset.getString("varattu_alkupvm");
                String loppu = tulokset.getString("varattu_loppupvm");

                String alkio = "ID: " + varaus_id + "  Tila: " + toimipiste_id + "  Ajalla: " + alku.substring(0,10) + " - " + loppu.substring(0,10);
                tilat.add(alkio);
            }
            varausLista.setItems(tilat);

        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }


    //Lisätään tila
    @FXML
    void tfTilaLisaaPainike(ActionEvent event) throws Exception {
        Toimipiste toimipisteOlio = new Toimipiste();

        toimipisteOlio.setPostitoimipaikka(tfTilaKaupunki.getText());
        toimipisteOlio.setNimi(tfTilaNimi.getText());
        toimipisteOlio.setLahiosoite(tfTilaOsoite.getText());
        toimipisteOlio.setPostinro(tfTilaPostinro.getText());
        toimipisteOlio.setToimipiste_id(Integer.parseInt(tfTilaToimipisteID.getText()));

        try {
            Statement lause = m_conn.createStatement();
            lause.executeUpdate("INSERT INTO toimipiste (toimipiste_id, nimi, lahiosoite, postitoimipaikka, postinro) "
                    + "VALUES (" + toimipisteOlio.getToimipiste_id() + ", '" + toimipisteOlio.getNimi() + "', '" + toimipisteOlio.getLahiosoite() + "', '" + toimipisteOlio.getPostitoimipaikka() + "', '" + toimipisteOlio.getPostinro() + "')");
            listaaTilat();
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Poistetaan tila
    @FXML
    void tfTilaPoistaPainike(ActionEvent event) throws Exception {
        Toimipiste toimipisteOlio = new Toimipiste();
        toimipisteOlio.setToimipiste_id(Integer.parseInt(tfTilaToimipisteID.getText()));

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vahvista poisto");
        alert.setHeaderText("Vahvista poisto");
        alert.setContentText("Oletko varma että haluat poistaa?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            try {
                Statement lause = m_conn.createStatement();
                lause.executeUpdate("DELETE FROM toimipiste WHERE toimipiste_id = " + toimipisteOlio.getToimipiste_id());
                tfTilaNimi.clear(); tfTilaOsoite.clear(); tfTilaKaupunki.clear(); tfTilaPostinro.clear(); tfTilaToimipisteID.clear();
                listaaTilat();
                listaaVaraukset();
            } catch (Exception e) {
                System.err.println("Virhe");
                System.err.println(e.getMessage());
            }
        }
    }

    //Haetaan tila
    @FXML
    void haeTilaPainike(ActionEvent event) throws Exception {
        haeTilat();
    }

    public void haeTilat() {
        Toimipiste toimipisteOlio = new Toimipiste();
        toimipisteOlio.setToimipiste_id(Integer.parseInt(tfTilaHaku.getText()));

        if (!tfTilaHaku.equals("")) {
            try {
                Statement lause = m_conn.createStatement();
                ResultSet tulokset = lause.executeQuery("SELECT * FROM toimipiste " +
                        "WHERE toimipiste_id = " + toimipisteOlio.getToimipiste_id());
                tulokset.next();
                int toimipiste_id = tulokset.getInt("toimipiste_id");
                String nimi = tulokset.getString("nimi");
                String lahiosoite = tulokset.getString("lahiosoite");
                String postitoimipaikka = tulokset.getString("postitoimipaikka");
                String postinro = tulokset.getString("postinro");
                String email = tulokset.getString("email");
                String puhelinnro = tulokset.getString("puhelinnro");

                tfTilaToimipisteID.setText(Integer.toString(toimipiste_id));
                tfTilaNimi.setText(nimi);
                tfTilaOsoite.setText(lahiosoite);
                tfTilaKaupunki.setText(postitoimipaikka);
                tfTilaPostinro.setText(postinro);

                tilanTulevatVaraukset();

            } catch (Exception e) {
                System.err.println("Virhe");
                System.err.println(e.getMessage());
            }
        }
    }

    //Muutetaan tilaa
    @FXML
    void tilaMuutaTietojaPainike(ActionEvent event) throws Exception {
        Toimipiste toimipisteOlio = new Toimipiste();

        toimipisteOlio.setPostitoimipaikka(tfTilaKaupunki.getText());
        toimipisteOlio.setNimi(tfTilaNimi.getText());
        toimipisteOlio.setLahiosoite(tfTilaOsoite.getText());
        toimipisteOlio.setPostinro(tfTilaPostinro.getText());
        toimipisteOlio.setToimipiste_id(Integer.parseInt(tfTilaToimipisteID.getText()));

        try {
            Statement lause = m_conn.createStatement();
            lause.executeUpdate("UPDATE toimipiste "
                    + "SET nimi = '" + toimipisteOlio.getNimi() + "', lahiosoite = '" + toimipisteOlio.getLahiosoite()
                    + "', postitoimipaikka = '" + toimipisteOlio.getPostitoimipaikka() + "', postinro = '" + toimipisteOlio.getPostinro()
                    + "', email = '" + toimipisteOlio.getEmail() + "', puhelinnro = '" + toimipisteOlio.getPuhelinnro()
                    + "' WHERE toimipiste_id = " + toimipisteOlio.getToimipiste_id());
            listaaTilat();
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Listataan tilat
    @FXML
    void listaaTilatPainike(ActionEvent event) throws Exception {
        listaaTilat();
    }

    public void listaaTilat() {
        ObservableList<String> tilat = FXCollections.observableArrayList();
        try {
            Statement lause = m_conn.createStatement();
            ResultSet tulokset = lause.executeQuery("SELECT * FROM toimipiste");
            while (tulokset.next()) {
                int toimipiste_id = tulokset.getInt("toimipiste_id");
                String nimi = tulokset.getString("nimi");
                String lahiosoite = tulokset.getString("lahiosoite");
                String postitoimipaikka = tulokset.getString("postitoimipaikka");
                String postinro = tulokset.getString("postinro");

                String alkio = toimipiste_id + " " + nimi + " " + lahiosoite + " " + postitoimipaikka + " " + postinro;
                tilat.add(alkio);
            }
            lvTila.setItems(tilat);

        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Listataan tulevat varaukset
    @FXML
    void tulevatVarauksetPainike(ActionEvent event) throws Exception {
        tilanTulevatVaraukset();
    }

    public void tilanTulevatVaraukset() {
        Toimipiste olio = new Toimipiste();
        olio.setToimipiste_id(Integer.parseInt(tfTilaToimipisteID.getText()));

        ObservableList<String> tilat = FXCollections.observableArrayList();
        try {
            Statement lause = m_conn.createStatement();
            ResultSet tulokset = lause.executeQuery("SELECT * FROM varaus WHERE toimipiste_id = " + olio.getToimipiste_id());
            while (tulokset.next()) {
                int varaus_id = tulokset.getInt("varaus_id");
                int asiakas_id = tulokset.getInt("asiakas_id");
                int toimipiste_id = tulokset.getInt("toimipiste_id");
                String alku = tulokset.getString("varattu_alkupvm");
                String loppu = tulokset.getString("varattu_loppupvm");

                String alkio = "Varaus_ID " + varaus_id + ", Aikana " + alku.substring(0,10) + " - " + loppu.substring(0,10);
                System.out.println(alkio);
                tilat.add(alkio);
            }
            lvTulevatVaraukset.setItems(tilat);

        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Raportin tekeminen
    @FXML
    void raporttiTulostaPainike(ActionEvent event) throws Exception {
        ToimipisteidenVaraus varausOlio = new ToimipisteidenVaraus();
        varausOlio.setVarattu_alkupvm(tfRaporttiAlku.getText());
        varausOlio.setVarattu_loppupvm(tfRaporttiLoppu.getText());

        try {
            File tiedosto = new File("Varausten raportti.txt");
            FileWriter fw = new FileWriter(tiedosto, true);
            BufferedWriter bw = new BufferedWriter(fw);

            Statement lause = m_conn.createStatement();
            ResultSet tulokset = lause.executeQuery("SELECT * FROM varaus WHERE varattu_alkupvm > " + '"' + varausOlio.getVarattu_alkupvm() + '"' + " AND varattu_loppupvm < " + '"' + varausOlio.getVarattu_loppupvm() + '"');
            while (tulokset.next()) {
                int varaus_id = tulokset.getInt("varaus_id");
                int asiakas_id = tulokset.getInt("asiakas_id");
                int toimipiste_id = tulokset.getInt("toimipiste_id");
                String alku = tulokset.getString("varattu_alkupvm");
                String loppu = tulokset.getString("varattu_loppupvm");

                bw.write("Varaus_ID: " + varaus_id + ", ");
                bw.write("Asiakas_ID: " + asiakas_id + ", ");
                bw.write("Toimipiste_ID: " + toimipiste_id + ", ");
                bw.write("Varaus alkaa: " + alku + ", ");
                bw.write("Varaus loppuu: " + loppu + "\n\n");
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Kirjoittaminen ei onnistunut");
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void raporttiTulostaPalvelut(ActionEvent event) throws Exception {
        ToimipisteidenVaraus varausOlio = new ToimipisteidenVaraus();
        varausOlio.setVarattu_alkupvm(tfRaporttiAlku.getText());
        varausOlio.setVarattu_loppupvm(tfRaporttiLoppu.getText());

        try {
            File tiedosto = new File("Palveluiden raportti.txt");
            FileWriter fw = new FileWriter(tiedosto, true);
            BufferedWriter bw = new BufferedWriter(fw);

            Statement lause = m_conn.createStatement();
            ResultSet tulokset = lause.executeQuery("SELECT * FROM varauksen_palvelut");
            while (tulokset.next()) {
                ResultSet tulokset2 = lause.executeQuery("SELECT nimi FROM palvelu " +
                        "WHERE palvelu_id = " + tulokset.getInt("palvelu_id"));
                tulokset2.next();
                int varaus_id = tulokset.getInt("varaus_id");
                int palvelu_id = tulokset.getInt("palvelu_id");
                int lkm = tulokset.getInt("lkm");
                String nimi = tulokset2.getString("nimi");

                bw.write("Varaus_ID: " + varaus_id + ", ");
                bw.write("Palvelu_ID: " + palvelu_id + ", ");
                bw.write("Palvelu: " + nimi + ", ");
                bw.write("Lukumäärä: " + lkm + "\n\n");
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Kirjoittaminen ei onnistunut");
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }


    // Lisää uuden asiakkaan
    @FXML
    void asiakkaanLisaysPainike(ActionEvent event) throws Exception {

        Asiakas asiakasOlio = new Asiakas();

        asiakasOlio.setAsiakasId(Integer.parseInt(asiakkaanID.getText()));
        asiakasOlio.setEtunimi(asiakkaanEtunimi.getText());
        asiakasOlio.setSukunimi(asiakkaanSukunimi.getText());
        asiakasOlio.setEmail(asiakkaanEmail.getText());
        asiakasOlio.setPuhelinnro(asiakkaanPuh.getText());
        asiakasOlio.setLahiosoite(asiakkaanOsoite.getText());
        asiakasOlio.setPostitoimipaikka(asiakkaanKaupunki.getText());
        asiakasOlio.setPostinro(asiakkaanPostinumero.getText());


        try {
            Statement lause = m_conn.createStatement();
            lause.executeUpdate("INSERT INTO Asiakas (asiakas_id, etunimi, sukunimi, " +
                    "lahiosoite, postitoimipaikka, postinro, email, puhelinnro) "
                    + "VALUES (" + asiakasOlio.getAsiakasId()
                    + ", '" + asiakasOlio.getEtunimi()
                    + "', '" + asiakasOlio.getSukunimi()
                    + "', '" + asiakasOlio.getLahiosoite()
                    + "', '" + asiakasOlio.getPostitoimipaikka()
                    + "', '" + asiakasOlio.getPostinro()
                    + "', '" + asiakasOlio.getEmail()
                    + "', '" + asiakasOlio.getPuhelinnro() + "')");

            listaaAsiakkaat();
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Tallennetaan asiakas
    @FXML
    void tallennaAsiakasPainike(ActionEvent event) throws Exception {

        Asiakas asiakasOlio = new Asiakas();

        asiakasOlio.setAsiakasId(Integer.parseInt(asiakkaanID.getText()));
        asiakasOlio.setEtunimi(asiakkaanEtunimi.getText());
        asiakasOlio.setSukunimi(asiakkaanSukunimi.getText());
        asiakasOlio.setEmail(asiakkaanEmail.getText());
        asiakasOlio.setPuhelinnro(asiakkaanPuh.getText());
        asiakasOlio.setLahiosoite(asiakkaanOsoite.getText());
        asiakasOlio.setPostitoimipaikka(asiakkaanKaupunki.getText());
        asiakasOlio.setPostinro(asiakkaanPostinumero.getText());

        try {
            Statement lause = m_conn.createStatement();
            lause.executeUpdate("UPDATE asiakas "
                    + "SET etunimi = '" + asiakasOlio.getEtunimi()
                    + "', sukunimi = '" + asiakasOlio.getSukunimi()
                    + "', lahiosoite = '" + asiakasOlio.getLahiosoite()
                    + "', postitoimipaikka = '" + asiakasOlio.getPostitoimipaikka()
                    + "', postinro = '" + asiakasOlio.getPostinro()
                    + "', email = '" + asiakasOlio.getEmail()
                    + "', puhelinnro = '" + asiakasOlio.getPuhelinnro()
                    + "' WHERE asiakas_id = " + asiakasOlio.getAsiakasId());
            listaaAsiakkaat();
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Poistetaan asiakas
    @FXML
    void poistaAsiakasPainike(ActionEvent event) throws Exception {
        Asiakas olio = new Asiakas();
        olio.setAsiakasId(Integer.parseInt(asiakkaanID.getText()));

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vahvista poisto");
        alert.setHeaderText("Vahvista poisto");
        alert.setContentText("Oletko varma että haluat poistaa?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            try {
                Statement lause = m_conn.createStatement();
                lause.executeUpdate("DELETE FROM asiakas WHERE asiakas_id = " + olio.getAsiakasId());
                listaaAsiakkaat();
            } catch (Exception e) {
                System.err.println("Virhe");
                System.err.println(e.getMessage());
            }
        }
    }

    @FXML
    void asiakasHakuPainike(ActionEvent event) throws Exception {
        haeAsiakas();
    }
    public void haeAsiakas() {
        Asiakas olio = new Asiakas();
        olio.setAsiakasId(Integer.parseInt(haeAsiakas.getText()));

        if (!haeAsiakas.equals("")) {
            try {
                Statement lause = m_conn.createStatement();
                ResultSet tulokset = lause.executeQuery("SELECT * FROM asiakas " +
                        "WHERE asiakas_id = " + olio.getAsiakasId());
                tulokset.next();
                int asiakas_id = tulokset.getInt("asiakas_id");
                String etunimi = tulokset.getString("etunimi");
                String sukunimi = tulokset.getString("sukunimi");
                String lahiosoite = tulokset.getString("lahiosoite");
                String postitoimipaikka = tulokset.getString("postitoimipaikka");
                String postinro = tulokset.getString("postinro");
                String email = tulokset.getString("email");
                String puhelinnro = tulokset.getString("puhelinnro");

                asiakkaanID.setText(Integer.toString(asiakas_id));
                asiakkaanEtunimi.setText(etunimi);
                asiakkaanSukunimi.setText(sukunimi);
                asiakkaanOsoite.setText(lahiosoite);
                asiakkaanKaupunki.setText(postitoimipaikka);
                asiakkaanPostinumero.setText(postinro);
                asiakkaanEmail.setText(email);
                asiakkaanPuh.setText(puhelinnro);

                listaaAsiakkaanVaraukset();

            } catch (Exception e) {
                System.err.println("Virhe");
                System.err.println(e.getMessage());
            }
        }


    }

    @FXML
    void asiakasListausPainike(ActionEvent event) throws Exception {
        listaaAsiakkaat();
    }

    public void listaaAsiakkaat() throws Exception {
        ObservableList<String> tilat = FXCollections.observableArrayList();
        try {
            Statement lause = m_conn.createStatement();
            ResultSet tulokset = lause.executeQuery("SELECT * FROM asiakas");
            while (tulokset.next()) {
                int asiakas_id = tulokset.getInt("asiakas_id");
                String etunimi = tulokset.getString("etunimi");
                String sukunimi = tulokset.getString("sukunimi");
                String lahiosoite = tulokset.getString("lahiosoite");
                String postitoimipaikka = tulokset.getString("postitoimipaikka");
                String postinro = tulokset.getString("postinro");
                String email = tulokset.getString("email");
                String puhelinnro = tulokset.getString("puhelinnro");

                String alkio = asiakas_id + " " + etunimi + " " + sukunimi
                        + " " + lahiosoite + " " + postitoimipaikka
                        + " " + postinro + " " + email + " " + puhelinnro;
                tilat.add(alkio);
            }
            asiakasListView.setItems(tilat);

        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    //Listataan asiakkaan varaukset
    @FXML
    void asiakkaanVarauksetPainike(ActionEvent event) throws Exception {
        listaaAsiakkaanVaraukset();
    }

    public void listaaAsiakkaanVaraukset() {
        ObservableList<String> varaukset = FXCollections.observableArrayList();
        Asiakas olio = new Asiakas();
        olio.setAsiakasId(Integer.parseInt(asiakkaanID.getText()));
        try {
            Statement lause = m_conn.createStatement();
            ResultSet tulokset = lause.executeQuery("SELECT * FROM varaus WHERE " +
                    "asiakas_id = " + olio.getAsiakasId());
            while (tulokset.next()) {
                String varausId = tulokset.getString("varaus_id");
                String alku = tulokset.getString("varattu_alkupvm");
                String loppu = tulokset.getString("varattu_loppupvm");

                String varausString = "VarausID: " + varausId + " |  Ajalle " + alku.substring(0,10) + " - " + loppu.substring(0,10);
                varaukset.add(varausString);
            }
            lvAsiakkaanVaraukset.setItems(varaukset);

        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    public void listaaPalvelut() throws Exception {
        try {
            palvelulista.clear();
            Statement palveluStatement = m_conn.createStatement();
            ResultSet palveluResultSet = palveluStatement.executeQuery("SELECT * FROM Palvelu");

            while (palveluResultSet.next()) {
                int palvelu_id = palveluResultSet.getInt("palvelu_id");
                String nimi = palveluResultSet.getString("nimi");
                double hinta = palveluResultSet.getDouble("hinta");
                String kuvaus = palveluResultSet.getString("kuvaus");

                PalveluEsim palvelu = new PalveluEsim(palvelu_id, nimi, hinta, kuvaus);
                palvelulista.add(palvelu);
            }
            palveluListView.setItems(palvelulista);
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    public void listaaLaskut() throws Exception {
        try {
            laskulista.clear();
            Statement laskuStatement = m_conn.createStatement();
            ResultSet laskuResultSet = laskuStatement.executeQuery("SELECT * FROM Lasku");

            while (laskuResultSet.next()) {
                int lasku_id = laskuResultSet.getInt("lasku_id");
                String nimi = laskuResultSet.getString("nimi");
                String sahkoposti = laskuResultSet.getString("sahkoposti");
                String puhelinnumero = laskuResultSet.getString("puhelinnumero");
                double summa = laskuResultSet.getDouble("summa");
                double alv = laskuResultSet.getDouble("alv");
                String erapaiva = laskuResultSet.getString("erapaiva");
                String paivalasku = laskuResultSet.getString("paivalasku");
                String laskukuvaus = laskuResultSet.getString("laskukuvaus");
                String laskutila = laskuResultSet.getString("laskutila");
                Lasku lasku = new Lasku(lasku_id, nimi, sahkoposti, puhelinnumero, summa, alv, erapaiva, paivalasku, laskukuvaus, laskutila);
                laskulista.add(lasku);
            }
        } catch (Exception e) {
            System.err.println("Virhe");
            System.err.println(e.getMessage());
        }
    }

    public void yhdista() throws Exception {
        m_conn = null;
        String url = "jdbc:mariadb://localhost:3306/vt";
        try {
            m_conn = DriverManager.getConnection(url, "root", "asdasd");
            System.out.println("Tietokanta yhteys luotu onnistuneestil");
        } catch (SQLException e) {
            m_conn = null;
            System.out.println("Failed to connect to the database");
            throw e;
        }
    }

    public void initialize() {
        try {
            yhdista();

            listaaAsiakkaat();
            listaaLaskut();
            listaaPalvelut();
            listaaVaraukset();
            listaaTilat();

            // Kuuntelija joka tekee asiakkaan haun enterin painalluksella
            haeAsiakas.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER & (!haeAsiakas.getText().equals(""))) {
                    haeAsiakas();
                }
            });

            // Kuuntelija joka tekee varauksen haun enterin painalluksella
            haeVaraus.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER & (!haeVaraus.getText().equals(""))) {
                    haeVaraus();
                }
            });

            // Kuuntelija joka tekee tilojen haun enterin painalluksella
            tfTilaHaku.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER & (!tfTilaHaku.getText().equals(""))) {
                    haeTilat();
                }
            });

            palveluListView.setItems(palvelulista);
            palveluListView.getSelectionModel().selectedItemProperty().addListener((observableValue, palvelu, t1) -> {
                PalveluEsim valittuPalvelu = palveluListView.getSelectionModel().getSelectedItem();
                if (valittuPalvelu != null) {
                    palvelunNimi.setText(valittuPalvelu.nimi());
                    kuvaus.setText(valittuPalvelu.kuvaus());
                    palvelunHinta.setText(Double.toString(valittuPalvelu.hinta()));
                }
            });

            laskuListView.setItems(laskulista);
            laskuListView.getSelectionModel().selectedItemProperty().addListener((observableValue, lasku, t1) -> {
                Lasku valittuLasku = laskuListView.getSelectionModel().getSelectedItem();
                if (valittuLasku != null) {
                    asiakaslaskuNimi.setText(valittuLasku.getNimi());
                    emailLasku.setText(valittuLasku.getSahkoposti());
                    puhelinroLasku.setText(valittuLasku.getPuhelinnumero());
                    summaLasku.setText(Double.toString(valittuLasku.getSumma()));
                    pvaLasku.setText(valittuLasku.getpvaLasku());
                    eraLasku.setText(valittuLasku.getEraLasku());
                    kuvausLasku.setText(valittuLasku.getKuvausLasku());
                    laskunTila.setText(valittuLasku.getLaskunTila());
                }
            });

            palveluHaku.textProperty().addListener((observable, oldValue, newValue) -> {
                FilteredList<PalveluEsim> filteredData = new FilteredList<>(palvelulista, palvelu -> {
                    String lowerCaseFilter = newValue.toLowerCase();
                    return palvelu.nimi().toLowerCase().contains(lowerCaseFilter) || palvelu.kuvaus().toLowerCase().contains(lowerCaseFilter);
                });

                palveluListView.setItems(filteredData);
            });

            laskuHaku.textProperty().addListener((observable, oldValue, newValue) -> {
                FilteredList<Lasku> filteredData = new FilteredList<>(laskulista, lasku -> {
                    String lowerCaseFilter = newValue.toLowerCase();
                    return lasku.getNimi().toLowerCase().contains(lowerCaseFilter);
                });
                laskuListView.setItems(filteredData);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




