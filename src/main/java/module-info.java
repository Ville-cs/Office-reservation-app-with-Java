module com.example.ohjelmistotuotanto1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.ohjelmistotuotanto1 to javafx.fxml;
    exports com.example.ohjelmistotuotanto1;
}