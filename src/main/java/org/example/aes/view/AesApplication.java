package org.example.aes.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.example.aes.logic.Aes;
import org.example.aes.logic.KeyParam;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.security.Key;

public class AesApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Ustawienia okna
        primaryStage.setTitle("AES Encryption/Decryption");

        // Tworzymy pola tekstowe do wprowadzenia danych
        TextArea inputDataField = new TextArea();
        inputDataField.setPromptText("Wprowadź dane wejściowe (ciąg znaków do zaszyfrowania)");

        TextField keyField = new TextField();
        keyField.setPromptText("Wprowadź klucz AES (16, 24 lub 32 bajty)");

        // Przyciski
        Button encryptButton = new Button("Szyfruj");
        Button decryptButton = new Button("Deszyfruj");
        Button saveButton = new Button("Zapisz do pliku");
        Button loadButton = new Button("Wczytaj z pliku");
        Button generateKeyButton = new Button("Generuj klucz AES");
        Label resultLabel = new Label("Wynik:");
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
                Aes aes = new Aes(keyParam, keyBytes);

                // Wykonanie szyfrowania
                byte[] encryptedData = aes.doEncryption(inputBytes);

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

        // Funkcja deszyfrowania po kliknięciu przycisku
        decryptButton.setOnAction(e -> {
            try {
                String encryptedDataHex = resultArea.getText();
                String keyText = keyField.getText();

                // Konwertowanie danych wejściowych (w postaci hex) na bajty
                byte[] encryptedData = new byte[encryptedDataHex.length() / 2];
                for (int i = 0; i < encryptedData.length; i++) {
                    encryptedData[i] = (byte) Integer.parseInt(encryptedDataHex.substring(i * 2, i * 2 + 2), 16);
                }
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
                Aes aes = new Aes(keyParam, keyBytes);

                // Wykonanie deszyfrowania
                byte[] decryptedData = aes.doDecryption(encryptedData);

                // Wyświetlenie wyniku
                resultArea.setText(new String(decryptedData));
            } catch (Exception ex) {
                resultArea.setText("Wystąpił błąd: " + ex.getMessage());
            }
        });

        // Funkcja zapisu do pliku
        saveButton.setOnAction(e -> {
            try {
                String resultText = resultArea.getText();
                if (resultText.isEmpty()) {
                    resultArea.setText("Brak danych do zapisania.");
                    return;
                }

                // Wybór lokalizacji pliku
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                File file = fileChooser.showSaveDialog(primaryStage);

                if (file != null) {
                    Files.write(file.toPath(), resultText.getBytes());
                    resultArea.setText("Dane zapisane do pliku.");
                }
            } catch (IOException ex) {
                resultArea.setText("Wystąpił błąd przy zapisie: " + ex.getMessage());
            }
        });

        // Funkcja wczytania z pliku
        loadButton.setOnAction(e -> {
            try {
                // Wybór pliku do wczytania
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    resultArea.setText(content);
                }
            } catch (IOException ex) {
                resultArea.setText("Wystąpił błąd przy wczytywaniu: " + ex.getMessage());
            }
        });

        // Funkcja generowania klucza AES
        generateKeyButton.setOnAction(e -> {
            try {
                // Generowanie klucza
                Key key = Aes.generateKey();
                keyField.setText(Base64.getEncoder().encodeToString(key.getEncoded()));
            } catch (Exception ex) {
                resultArea.setText("Wystąpił błąd przy generowaniu klucza: " + ex.getMessage());
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
                decryptButton,
                saveButton,
                loadButton,
                generateKeyButton,
                resultLabel,
                resultArea
        );

        // Tworzymy scenę
        Scene scene = new Scene(layout, 600, 500);

        // Ustawiamy scenę na oknie
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



