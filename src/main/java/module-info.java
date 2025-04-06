module org.example.aes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens org.example.aes to javafx.fxml;
    exports org.example.aes.logic;
    exports org.example.aes.view;
    opens org.example.aes.view to javafx.fxml;
}