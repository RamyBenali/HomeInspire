package com.example.demo.controller;

import com.example.demo.model.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class HelloController {

    @FXML
    private TextField idF;

    @FXML
    private PasswordField pwF;

    @FXML
    private Button connectBtn;

    @FXML
    public void initialize() {
        idF.setOnKeyPressed(this::handleEnterKey);
        pwF.setOnKeyPressed(this::handleEnterKey);
    }

    @FXML
    protected void onconnectBtnClick(ActionEvent event) {
        String username = idF.getText();
        String password = pwF.getText();

        if (isValidCredentials(username, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Connexion Réussie",
                    "Bienvenue " + employeeFullName + " !");
            try {
                Parent landingPage = FXMLLoader.load(getClass().getResource("/com/example/demo/landing-page.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(landingPage));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Connexion Échouée", "Identifiant ou mot de passe incorrect.");
            idF.setText("");
            pwF.setText("");
        }
    }

    private void handleEnterKey(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER -> connectBtn.fire();
        }
    }

    public static String employeeFullName;
    public static boolean admin;

    private boolean isValidCredentials(String username, String password) {
        String sql = "SELECT p.nom, p.prenom " +
                "FROM employe e " +
                "JOIN personne p ON e._idPers = p._idPers " +
                "WHERE e.email = ? AND e.motDePasse = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                employeeFullName = prenom + " " + nom;
                admin = Objects.equals(nom, "Administrateur");
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la connexion à la base de données.");
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
