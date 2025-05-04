package com.example.demo.controller;

import com.example.demo.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class bienDetailsController {
    @FXML
    private TableView<Client> addClient_tableView;
    @FXML
    private TableColumn<Client, Number> addClient_col_id;
    @FXML
    private TableColumn<Client, String> addClient_col_prenom;
    @FXML
    private TableColumn<Client, String> addClient_col_nom;
    @FXML
    private TableColumn<Client, String> addClient_col_numTel;
    @FXML
    private TableColumn<Client, String> addClient_col_dateNaiss;
    @FXML
    private TableColumn<Client, String> addClient_col_adresse;
    @FXML
    private TableColumn<Client, String> addClient_col_email;
    @FXML
    private TableColumn<Client, String> addClient_col_etatCivil;
    @FXML
    private TableColumn<Client, Double> addClient_col_salaire;
    @FXML
    private TableColumn<Client, String> addClient_col_situationPro;
    @FXML
    private TableColumn<Client, String> addClient_col_patrimoine;
    @FXML
    private TableColumn<Client, String> addClient_col_type;
    @FXML
    private BorderPane selectClient_pane;
    @FXML
    private BorderPane selectBien_pane;
    @FXML
    private BorderPane transac_pane;
    @FXML
    private ImageView imageViewBien; // Pour afficher l'image
    @FXML
    private Label labelAdresse;
    @FXML
    private Label labelDescription;
    @FXML
    private Label labelPrix;
    @FXML
    private Label labelDispo;
    @FXML
    private Label labelProprio;
    @FXML
    private Label labelType;
    @FXML
    private Button selectBien_retour;
    @FXML
    private Button selectBien_valider;
    @FXML
    private Button selectClient_back;
    @FXML
    private Button selectClient_next;
    @FXML
    private TextField addClient_search;
    @FXML
    private TextField transac_idBien;
    @FXML
    private TextField transac_idClient;
    @FXML
    private DatePicker transac_dateTransac;
    @FXML
    private ComboBox<String> transac_typeTransac;
    @FXML
    private DatePicker transac_dateDebut;
    @FXML
    private DatePicker transac_dateFin;
    @FXML
    private ImageView imageViewBien1;
    @FXML
    private Label labelAdresse1;
    @FXML
    private Label labelType1;
    @FXML
    private Label labelDispo1;
    @FXML
    private Label labelProprio1;
    @FXML
    private Label labelDescription1;
    @FXML
    private Label labelPrix1;
    @FXML
    private Label labelDateDebut;
    @FXML
    private Label labelDateFin;
    @FXML
    private TextField transac_montant;
    @FXML
    private TextField transac_nomClient;
    @FXML
    private Button selectClient_back1;
    @FXML
    private Button selectClient_next1;
    @FXML
    private Label labelTransacEnregistrer;
    @FXML
    private Button closeBtn;

    private ObservableList<Client> clientList;
    private ClientDAO clientDAO;
    private Transaction transaction;
    private TransactionDAO transactionDAO;
    private recherchePageController RecherchePageController;

    public void setRecherchePageController(recherchePageController RecherchePageController) {
        this.RecherchePageController = RecherchePageController;
    }

    @FXML
    public void setBienDetails(BienImmo bien) {
        Image image = new Image(bien.getImage());
        imageViewBien.setImage(image);
        labelAdresse.setText(bien.getAdresse());
        labelDescription.setText(bien.getDescription());
        labelDispo.setText(bien.getDispo());
        labelProprio.setText(bien.getproprietaire());
        labelType.setText(bien.getType());
        labelPrix.setText("Prix : " + bien.getPrix() + " DZD");
        String idBien = bien.getIdBien() + "";
        transac_idBien.setText(idBien);
        transac_idBien.setEditable(false);
        transac_idBien.setDisable(true);
        imageViewBien1.setImage(image);
        labelAdresse1.setText(bien.getAdresse());
        labelDescription1.setText(bien.getDescription());
        labelDispo1.setText(bien.getDispo());
        labelProprio1.setText(bien.getproprietaire());
        labelType1.setText(bien.getType());
        labelPrix1.setText("Prix : " + bien.getPrix() + " DZD");
    }

    @FXML
    public void onCloseBtn(ActionEvent event) throws SQLException {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onRetourBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void setupSearchFeature(TableView<Client> tableView, ObservableList<Client> clientList, TextField addClient_search) {
        FilteredList<Client> filteredData = new FilteredList<>(clientList, p -> true);

        addClient_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (client.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (client.getPrenom().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(client.getNumTel()).contains(lowerCaseFilter)) {
                    return true;
                } else if (client.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (client.getSituationPro().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (client.getPatrimoineImmo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (client.getType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Client> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    @FXML
    public void onValiderBtn(ActionEvent event) {
        selectBien_pane.setVisible(false);
        selectClient_pane.setVisible(true);
        Connection connection = DatabaseConnection.connect();
        clientDAO = new ClientDAO(connection);
        clientList = FXCollections.observableArrayList();

        try {
            clientList.clear();
            clientList.addAll(clientDAO.getAllClients());
        } catch (SQLException e) {
            showError("Erreur lors du chargement des clients : " + e.getMessage());
        }

        addClient_col_id.setCellValueFactory(cellData -> cellData.getValue().idClientProperty());
        addClient_col_prenom.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        addClient_col_nom.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        addClient_col_numTel.setCellValueFactory(cellData -> {
            Integer numTel = cellData.getValue().getNumTel();
            return new SimpleStringProperty(numTel != null ? numTel.toString() : "");
        });
        addClient_col_dateNaiss.setCellValueFactory(cellData -> {
            LocalDate dateNaiss = cellData.getValue().getDateNaiss();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new SimpleStringProperty(dateNaiss != null ? dateNaiss.format(formatter) : "");
        });
        addClient_col_adresse.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
        addClient_col_email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        addClient_col_etatCivil.setCellValueFactory(cellData -> cellData.getValue().etatCivilProperty());
        addClient_col_salaire.setCellValueFactory(cellData -> cellData.getValue().salaireProperty().asObject());
        addClient_col_situationPro.setCellValueFactory(cellData -> cellData.getValue().situationProProperty());
        addClient_col_patrimoine.setCellValueFactory(cellData -> cellData.getValue().patrimoineImmoProperty());
        addClient_col_type.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        addClient_tableView.setItems(clientList);

        setupSearchFeature(addClient_tableView, clientList, addClient_search);
    }

    @FXML
    public void onNextBtn(ActionEvent event) {
        Client selectedClient = addClient_tableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            int idClient = selectedClient.getIdClient();
            transac_idClient.setText(String.valueOf(idClient));
            transac_idClient.setEditable(false);
            transac_idClient.setDisable(true);
            transac_nomClient.setText(selectedClient.getNom() + " " + selectedClient.getPrenom());
            transac_nomClient.setEditable(false);
            transac_nomClient.setDisable(true);
            selectBien_pane.setVisible(false);
            selectClient_pane.setVisible(false);
            transac_pane.setVisible(true);
            transac_dateDebut.setVisible(false);
            transac_dateFin.setVisible(false);
            labelDateDebut.setVisible(false);
            labelDateFin.setVisible(false);
            transac_typeTransac.getItems().addAll("Vente", "Location");
            transac_typeTransac.setValue("Vente");
        } else {
            showError("Veuilez choisir un client de l'agence !");
        }
    }

    @FXML
    public void onTypeCombo(ActionEvent event) {
        if (transac_typeTransac.getSelectionModel().getSelectedItem().equals("Location")) {
            transac_dateDebut.setVisible(true);
            transac_dateFin.setVisible(true);
            labelDateDebut.setVisible(true);
            labelDateFin.setVisible(true);
        } else {
            transac_dateDebut.setVisible(false);
            transac_dateFin.setVisible(false);
            labelDateDebut.setVisible(false);
            labelDateFin.setVisible(false);
        }
    }

    @FXML
    public void onBackBtn1(ActionEvent event) {
        selectBien_pane.setVisible(false);
        selectClient_pane.setVisible(true);
        transac_pane.setVisible(false);
    }

    @FXML
    public void onBackBtn(ActionEvent event) {
        selectBien_pane.setVisible(true);
        selectClient_pane.setVisible(false);
        transac_pane.setVisible(false);
    }


    @FXML
    private void showErrorTF(TextField tf, boolean verif) {
        tf.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        showError("Le champ "+tf.getPromptText()+" ne respecte pas la syntaxe !");
        verif = false;
    }

    @FXML
    public void onEnregistrerBtn(MouseEvent event) {
        boolean verif = true;
        int idTransac = 1;

        transac_montant.setStyle(null);
        Connection connection = DatabaseConnection.connect();
        if (!transac_montant.getText().matches("^(0|[1-9]\\d*)(\\.\\d{1,2})?$")) {
            showErrorTF(transac_montant, verif);
        } else
            transactionDAO = new TransactionDAO(connection);
        try {
            Transaction transaction = new Transaction(
                    idTransac,
                    transac_dateTransac.getValue(),
                    Double.parseDouble(transac_montant.getText()),
                    transac_typeTransac.getValue(),
                    Integer.parseInt(transac_idBien.getText()),
                    Integer.parseInt(transac_idClient.getText()),
                    transac_dateDebut.getValue(),
                    transac_dateFin.getValue()
            );

            transactionDAO.ajouterTransaction(transaction);

            showInfo("Client ajouté avec succès !");
            transac_dateDebut.setDisable(true);
            transac_dateFin.setDisable(true);
            transac_montant.setDisable(true);
            transac_dateTransac.setDisable(true);
            transac_typeTransac.setDisable(true);
            selectClient_back1.setVisible(false);
            selectClient_next1.setVisible(false);
            labelTransacEnregistrer.setVisible(true);
            closeBtn.setVisible(true);
            BienImmoDAO.updateDisponibilite(Integer.parseInt(transac_idBien.getText()), "Non");

        } catch (NumberFormatException e) {
            showError("Erreur : certains champs numériques ne sont pas valides.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

