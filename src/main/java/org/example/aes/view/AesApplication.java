package org.example.aes.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.example.aes.logic.Aes;
import org.example.aes.logic.Helper;
import org.example.aes.logic.KeyParam;

import java.io.*;
import java.util.Objects;

public class AesApplication extends Application {

    byte[] result;
    byte[] input;
    private RadioButton radio128;
    private RadioButton radio192;
    private RadioButton radio256;

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


        // Create and configure radio buttons
        this.radio128 = new RadioButton("128-bit key");
        this.radio192 = new RadioButton("192-bit key");
        this.radio256 = new RadioButton("256-bit key");

        // Put them in a toggle group so only one is selected
        ToggleGroup keySizeGroup = new ToggleGroup();
        radio128.setToggleGroup(keySizeGroup);
        radio192.setToggleGroup(keySizeGroup);
        radio256.setToggleGroup(keySizeGroup);

        // Choose 128-bit by default
        radio128.setSelected(true);
        // Funkcja szyfrowania po kliknięciu przycisku
        encryptButton.setOnAction(_ -> {
            try {
                String keyText = keyField.getText();
                byte[] inputBytes;
                // Konwertowanie danych wejściowych na bajty
                inputBytes = Objects.requireNonNullElseGet(input, () -> inputDataField.getText().getBytes());
                byte[] keyBytes = keyText.getBytes();

                // Wybór rozmiaru klucza
                KeyParam keyParam = chooseKeyParam(keyBytes.length);
                if (keyParam == null){
                    resultArea.setText("Klucz musi mieć 16, 24 lub 32 bajty.");
                    return;
                }


                // Inicjalizacja algorytmu AES
                Aes aes = new Aes(keyParam, inputBytes, keyBytes);

                // Wykonanie szyfrowania
                byte[] encryptedData = aes.doEncryption();
                result = encryptedData;
                // Konwersja wyniku do postaci hex

                String encText = Helper.bytesToHex(encryptedData);
                // Wyświetlenie wyniku
                resultArea.setText(encText);
                input = null;
            } catch (Exception ex) {
                resultArea.setText("Wystąpił błąd: " + ex.getMessage());
            }
        });

        // Funkcja deszyfrowania po kliknięciu przycisku
        decryptButton.setOnAction(_ -> {
            try {
                String keyText = keyField.getText();
                byte[] keyBytes = keyText.getBytes();
                // Konwertowanie danych wejściowych (w postaci hex) na bajty (tylko w przypadku tekstu nie z pliku)
                byte[] encryptedData;
                if (input == null){
                    encryptedData = Helper.hexToBytes(inputDataField.getText());
                }else {
                    encryptedData = input;
                }

                KeyParam keyParam = chooseKeyParam(keyBytes.length);
                if (keyParam == null){
                    resultArea.setText("Klucz musi mieć 16, 24 lub 32 bajty.");
                    return;
                }

                Aes aes = new Aes(keyParam, encryptedData, keyBytes);

                byte[] decryptedData = aes.doDecryption();
                result = decryptedData;
                resultArea.setText(new String(decryptedData));
                input = null;
            } catch (Exception ex) {
                resultArea.setText("Wystąpił błąd: " + ex.getMessage());
            }
        });

        saveButton.setOnAction(_ -> {
            try {
                String resultText = resultArea.getText();
                if (resultText.isEmpty()) {
                    resultArea.setText("Brak danych do zapisania.");
                    return;
                }

                // Wybór lokalizacji pliku
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(primaryStage);

                if (file != null) {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(result);
                    fos.close();
                    resultArea.setText("Dane zapisane do pliku.");
                }
            } catch (IOException ex) {
                resultArea.setText("Wystąpił błąd przy zapisie: " + ex.getMessage());
            }
        });

        // Funkcja wczytania z pliku
        loadButton.setOnAction(_ -> {
            try {
                // Wybór pliku do wczytania
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null) {
                    FileInputStream fis = new FileInputStream(file);
                    int length = fis.available();
                    input = new byte[length];
                    fis.read(input);
                    fis.close();

                    inputDataField.setText(new String(input));
                }
            } catch (IOException ex) {
                resultArea.setText("Wystąpił błąd przy wczytywaniu: " + ex.getMessage());
            }
        });

        // Funkcja generowania klucza AES
        generateKeyButton.setOnAction(_ -> {
            try {
                if (radio128.isSelected()) {
                    // 128-bit key (16 bytes)
                    keyField.setText("1234567890abcdef");
                } else if (radio192.isSelected()) {
                    // 192-bit key (24 bytes)
                    keyField.setText("1234567890abcdef12345678");
                } else if (radio256.isSelected()) {
                    // 256-bit key (32 bytes)
                    keyField.setText("1234567890abcdef1234567890abcdef");
                }
            } catch (Exception ex) {
                resultArea.setText("Wystąpił błąd przy generowaniu klucza: " + ex.getMessage());
            }
        });

        // Układ interfejsu
        HBox radioBox = new HBox(20, radio128, radio192, radio256);
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
                new Label("Wybierz rozmiar generowanego klucza:"),
                radioBox,            // Our radio buttons
                resultLabel,
                resultArea
        );

        // Tworzymy scenę
        Scene scene = new Scene(layout, 600, 500);

        // Ustawiamy scenę na oknie
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private KeyParam chooseKeyParam(int length) {
        if (length == 16) {
            return KeyParam.SIZE_128;
        } else if (length == 24) {
            return KeyParam.SIZE_192;
        } else if (length == 32){
            return KeyParam.SIZE_256;
        }else {
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}



