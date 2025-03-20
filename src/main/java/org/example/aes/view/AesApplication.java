package org.example.aes.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.example.aes.logic.Aes;
import org.example.aes.logic.KeyParam;

public class AesApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Ustawienia okna
        primaryStage.setTitle("AES Encryption");

        // Tworzymy pola tekstowe do wprowadzenia danych
        TextArea inputDataField = new TextArea();
        inputDataField.setPromptText("Wprowadź dane wejściowe (ciąg znaków do zaszyfrowania)");

        TextField keyField = new TextField();
        keyField.setPromptText("Wprowadź klucz AES (16, 24 lub 32 bajty)");

        // Przyciski
        Button encryptButton = new Button("Szyfruj");
        Label resultLabel = new Label("Wynik szyfrowania: ");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // Funkcja szyfrowania po kliknięciu przycisku
        encryptButton.setOnAction(e -> {
            try {
                String inputData = inputDataField.getText();
                String keyText = keyField.getText();

                // Konwertowanie danych wejściowych na bajty
                byte[] inputBytes = inputData.getBytes();
                byte[] keyBytes = keyText.getBytes();

                // Sprawdzenie długości klucza
                if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
                    resultArea.setText("Klucz musi mieć 16, 24 lub 32 bajty.");
                    return;
                }

                // Wybór rozmiaru klucza
                KeyParam keyParam;
                if (keyBytes.length == 16) {
                    keyParam = KeyParam.SIZE_128;
                } else if (keyBytes.length == 24) {
                    keyParam = KeyParam.SIZE_192;
                } else {
                    keyParam = KeyParam.SIZE_256;
                }

                // Inicjalizacja algorytmu AES
                Aes aes = new Aes(keyParam, inputBytes, keyBytes);

                // Wykonanie szyfrowania
                byte[] encryptedData = aes.doEncryption();

                // Konwersja wyniku do postaci hex
                StringBuilder hexResult = new StringBuilder();
                for (byte b : encryptedData) {
                    hexResult.append(String.format("%02X", b));
                }

                // Wyświetlenie wyniku
                resultArea.setText(hexResult.toString());
            } catch (Exception ex) {
                resultArea.setText("Wystąpił błąd: " + ex.getMessage());
            }
        });

        // Układ interfejsu
        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(20));
        layout.getChildren().addAll(
                new Label("Dane wejściowe:"),
                inputDataField,
                new Label("Klucz AES:"),
                keyField,
                encryptButton,
                resultLabel,
                resultArea
        );

        // Tworzymy scenę
        Scene scene = new Scene(layout, 400, 300);

        // Ustawiamy scenę na oknie
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


