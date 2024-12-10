package com.example.ohjelmistotuotanto1;
//
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

/// EI OLE KÄYTÖSSÄ
/*
public class PalveluController {

    // Test data for checking the functionality
    ObservableList<PalveluEsim> palvelulista = FXCollections.observableArrayList();

    @FXML
    private Connection m_conn;

    // ListView for displaying Palvelus
    @FXML
    private ListView<PalveluEsim> palveluListView;

    // TextFields for adding a new Palvelu
    @FXML
    private TextField palvelunNimi;

    @FXML
    private TextField palvelunHinta;

    // TextArea for adding a new Kuvaus
    @FXML
    private TextArea kuvaus;
    private int toimipiste_id;

    public PalveluController(ListView<PalveluEsim> palveluListView) {
        this.palveluListView = palveluListView;
    }

    // Method to establish database connection
    public void yhdista1() throws SQLException, Exception {
        m_conn = null;
        String url = "jdbc:mariadb://localhost:3308/vt";
        try {
            m_conn = DriverManager.getConnection(url, "root", "12345");
            System.out.println("Database connection successful");

            // Create a table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Palvelu ("
                    + "palvelu_id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "toimipiste_id INT,"
                    + "nimi VARCHAR(40),"
                    + "tyyppi INT,"
                    + "kuvaus VARCHAR(255),"
                    + "hinta DOUBLE(8,2) NOT NULL,"
                    + "alv DOUBLE(8,2) NOT NULL,"
                    + "FOREIGN KEY (toimipiste_id) REFERENCES Toimipiste (toimipiste_id) ON DELETE CASCADE"
                    + ")";
            Statement statement = m_conn.createStatement();
            statement.executeUpdate(createTableQuery);
            statement.close();
        } catch (SQLException e) {
            m_conn = null;
            System.out.println("Failed to connect to the database");
            throw e;
        }

        Statement statement = m_conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT palvelu_id, nimi, hinta, kuvaus FROM Palvelu");

        while (resultSet.next()) {
            int palvelu_id = resultSet.getInt("palvelu_id");
            String nimi = resultSet.getString("nimi");
            double hinta = resultSet.getDouble("hinta");
            String kuvaus = resultSet.getString("kuvaus");
            int toimipiste_id = resultSet.getInt("toimipiste_id");

            PalveluEsim palvelu = new PalveluEsim(palvelu_id, nimi, hinta, kuvaus, toimipiste_id);
            palvelulista.add(palvelu);
        }


        resultSet.close();
        statement.close();
    }


    // Method to add a new Palvelu to the list
    @FXML
    private void lisaaUusiPressed() {
        palvelulista.add(
                new PalveluEsim(0, palvelunNimi.getText(), Double.parseDouble(palvelunHinta.getText()), kuvaus.getText(), toimipiste_id));

        palvelunNimi.clear();
        palvelunHinta.clear();
        kuvaus.clear();


        System.out.println("Uusi palvelu lisättiin");
    }

    // Method to delete a Palvelu from the list
    @FXML
    private void poistaPalvelu() {
        PalveluEsim poistettava = palveluListView.getSelectionModel().getSelectedItem();
        if (poistettava != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Vahvista poisto");
            alert.setHeaderText("Vahvista poisto");
            alert.setContentText("Oletko varma että haluat poistaa?!");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                palvelulista.remove(poistettava);
                System.out.println("Palvelu poistettiin");
            }
        }
    }
    // Method to save the updated Palvelu information
    @FXML
    private void tallennaPalvelu() {
        PalveluEsim muokattavaPalvelu = palveluListView.getSelectionModel().getSelectedItem();
        muokattavaPalvelu.setNimi(palvelunNimi.getText());
        muokattavaPalvelu.setHinta(Double.parseDouble(palvelunHinta.getText()));
        muokattavaPalvelu.setKuvaus(kuvaus.getText());

        System.out.println("Palvelun tiedot tallennettiin");
    }

    // Method to initialize the ListView
    @FXML
    private void initialize() {

        palveluListView.setItems(palvelulista);

        // Clear the current list and query the data from the database again
        palvelulista.clear();
        try {
            yhdista1();
        } catch (Exception e) {
            e.printStackTrace();
        }

        palveluListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PalveluEsim>() {
            @Override
            public void changed(ObservableValue<? extends PalveluEsim> observableValue, PalveluEsim asiakas, PalveluEsim t1) {
                PalveluEsim valittuPalvelu = palveluListView.getSelectionModel().getSelectedItem();
                palvelunNimi.setText(valittuPalvelu.getNimi());
                palvelunHinta.setText(Double.toString(valittuPalvelu.getHinta()));
                kuvaus.setText(valittuPalvelu.getKuvaus());
            }
        });
    }
}
*/