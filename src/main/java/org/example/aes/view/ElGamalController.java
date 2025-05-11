package org.example.aes.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.aes.logic.ElGamal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ElGamalController {
    public TextField pField;
    public TextField gField;
    public TextField xField;
    public TextField yField;
    public TextArea messageArea;
    public TextArea signatureArea;
    public Button btnLoadMessage;
    public Button btnSaveSignature;
    public Button btnLoadSignature;
    public Button btnGenerateKeys;

    ElGamal elGamal;
    ElGamal.Signature signature;

    private Stage getStage() {
        return (Stage) pField.getScene().getWindow();
    }

    private void showAlert(Alert.AlertType type, String header, String msg) {
        Alert alert = new Alert(type);
        alert.initOwner(getStage());
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void onGenerateKeys(ActionEvent actionEvent) {
        if (elGamal == null) {
            elGamal = new ElGamal(512);
        } else {
            elGamal.generateKeys(512);
        }
        pField.setText(elGamal.p.toString(16));
        gField.setText(elGamal.g.toString(16));
        xField.setText(elGamal.x.toString(16));
        yField.setText(elGamal.y.toString(16));
    }

    public void onReadDataFromFile(ActionEvent actionEvent)  {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(getStage());

        if (file != null) {
            try {
                FileInputStream fis = new FileInputStream(file);
                int length = fis.available();
                byte[] input = new byte[length];
                fis.read(input);
                fis.close();
                messageArea.setText(new String(input));
            }catch (IOException ex){
                showAlert(Alert.AlertType.ERROR, "Błąd wczytywania", ex.getMessage());
            }

        }
    }

    public void onSignatureWriteToFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Zapisać podpis");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text", "*.sig"));
        File file = fc.showSaveDialog(getStage());
        if (file == null) return;
        try {
            Files.writeString(file.toPath(), signatureArea.getText(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Błąd zapisu", ex.getMessage());
        }
    }

    public void onSignatureReadFromFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Wczytać podpis");
        File file = fc.showOpenDialog(getStage());
        if (file == null) return;
        try {
            String sigText = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            signatureArea.setText(sigText.trim());
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Błąd wczytywania", ex.getMessage());
        }
    }

    public void onSign(ActionEvent actionEvent) {
        try {
            ensureElGamalFromFields();
            String msg = messageArea.getText();
            ElGamal.Signature sig = elGamal.sign(msg);
            signatureArea.setText(sig.r().toString(16) + "\n" + sig.s().toString(16));
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Błąd Podpisu", ex.getMessage());
        }
    }

    public void onVerificate(ActionEvent actionEvent) {
        try {
            ensureElGamalFromFields();
            String[] lines = signatureArea.getText().trim().split("\n|\r");
            if (lines.length < 2) throw new IllegalArgumentException("Потрібно два рядки: r та s у hex-форматі");
            BigInteger r = new BigInteger(lines[0].trim(), 16);
            BigInteger s = new BigInteger(lines[1].trim(), 16);
            ElGamal.Signature sig = new ElGamal.Signature(r, s);
            boolean ok = elGamal.verify(messageArea.getText(), sig);
            showAlert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    "Wynik weryfikacji",
                    ok ? "Podpis jest zgodny" : "Podpis nie jest zgodny");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Błąd weryfikacji", ex.getMessage());
        }
    }

    private void ensureElGamalFromFields() {
        if (elGamal == null) {
            elGamal = new ElGamal(512);
        }
        try {
            BigInteger p = new BigInteger(pField.getText().trim(), 16);
            BigInteger g = new BigInteger(gField.getText().trim(), 16);
            BigInteger x = new BigInteger(xField.getText().trim(), 16);
            BigInteger y = new BigInteger(yField.getText().trim(), 16);

            elGamal.p = p;
            elGamal.g = g;
            elGamal.x = x;
            elGamal.y = y;
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("p, g, x, y Powinny być liczbami hex");
        }
    }
}
