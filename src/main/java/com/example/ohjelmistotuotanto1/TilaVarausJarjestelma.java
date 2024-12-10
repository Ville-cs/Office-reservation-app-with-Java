package com.example.ohjelmistotuotanto1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class TilaVarausJarjestelma extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(TilaVarausJarjestelma.class.getResource("tilavaraus-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Tilavarausjärjestelmä!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}