package com.example.demo.controller;

import com.example.demo.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.awt.Color.RED;

public class GestionClientController {

    @FXML
    public Pane comptePane1;
    @FXML
    public Pane decoPane;
    @FXML
    public Label labelComptePane1;
    @FXML
    public Label labelComptePane2;
    @FXML
    public Label biensNav;
    @FXML
    public Label acceuilNav;
    @FXML
    public Label employeNav;

    @FXML
    private AnchorPane main_form;
    @FXML
    private AnchorPane addClient_form;
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
    private TextField addClient_search;
    @FXML
    private Button addClient_addBtn;
    @FXML
    private Button addClient_updateBtn;
    @FXML
    private Button addClient_deleteBtn;
    @FXML
    private Button addClient_clearBtn;

    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    @FXML
    private TextField addClient_prenom;
    @FXML
    private TextField addClient_nom;
    @FXML
    private TextField addClient_numTel;
    @FXML
    private DatePicker addClient_dateNaiss;
    @FXML
    private TextField addClient_adresse;
    @FXML
    private TextField addClient_email;
    @FXML
    private ChoiceBox<String> addClient_etatCivil;
    @FXML
    private TextField addClient_salaire;
    @FXML
    private ComboBox<String> addClient_situationPro;
    @FXML
    private TextField addClient_patrimoine;
    @FXML
    private ComboBox<String> addClient_type;

    private PersonneDAO personneDAO;
    private ClientDAO clientDAO;

    private ObservableList<Client> clientList;

    public void initialize() {
        labelComptePane1.setText(HelloController.employeeFullName);
        labelComptePane2.setText(HelloController.employeeFullName);
        addClient_etatCivil.getItems().addAll("Célibataire", "Marié(e)", "Divorcé(e)", "Veuf/Veuve");
        addClient_etatCivil.setValue("Célibataire");

        addClient_situationPro.getItems().addAll("Salarié", "Indépendant", "Étudiant", "Retraité", "Sans emploi");
        addClient_situationPro.setValue("Salarié");

        addClient_type.getItems().addAll("Client", "Propriétaire");
        addClient_type.setValue("Client");

        Connection connection = DatabaseConnection.connect();
        personneDAO = new PersonneDAO(connection);
        clientDAO = new ClientDAO(connection);
        clientList = FXCollections.observableArrayList();
        configureTable();
        loadClients();
        setupSearchFeature(addClient_tableView, clientList, addClient_search);
        miseajourClient();
    }

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
                }else if (client.getPatrimoineImmo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if(client.getType().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }

                return false;
            });
        });

        SortedList<Client> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }

    private void configureTable() {
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
    }

    private void loadClients() {
        try {
            clientList.clear();
            clientList.addAll(clientDAO.getAllClients());
        } catch (SQLException e) {
            showError("Erreur lors du chargement des clients : " + e.getMessage());
        }
    }

    @FXML
    private void resetTF() {
        addClient_nom.setStyle(null);
        addClient_prenom.setStyle(null);
        addClient_numTel.setStyle(null);
        addClient_email.setStyle(null);
        addClient_salaire.setStyle(null);
        addClient_dateNaiss.setStyle(null);
    }

    @FXML
    private void showErrorTF(TextField tf, boolean verif){
        tf.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        showError("Le champ "+tf.getPromptText()+" ne respecte pas la syntaxe !");
        verif = false;
    }

    @FXML
    private void addClient() {
        resetTF();
        boolean verif = true;
        if(!addClient_prenom.getText().matches("^[a-zA-ZÀ-ÿ\\s'-]+$")) {
            showErrorTF(addClient_prenom, verif);
        }else if (!addClient_nom.getText().matches("^[a-zA-ZÀ-ÿ\\s'-]+$")) {
                showErrorTF(addClient_nom, verif);
        }else if(!addClient_numTel.getText().matches("^\\d{10}$")) {
            showErrorTF(addClient_numTel, verif);
        }else if(addClient_dateNaiss.getValue() == null){
            addClient_dateNaiss.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showError("Veuillez donner une date de naissance valide !");
        } else if (!addClient_email.getText().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showErrorTF(addClient_email, verif);
        }else if(!addClient_salaire.getText().matches("^(0|[1-9]\\d*)(\\.\\d{1,2})?$")) {
            showErrorTF(addClient_salaire, verif);
        }else {

            try {
                    Personne personne = new Personne(
                            addClient_nom.getText(),
                            addClient_prenom.getText(),
                            Integer.parseInt(addClient_numTel.getText()),
                            addClient_dateNaiss.getValue(),
                            addClient_adresse.getText()
                    );

                    int personneId = personneDAO.addPersonne(personne);

                    Client client = new Client(
                            personneId,
                            addClient_nom.getText(),
                            addClient_prenom.getText(),
                            Integer.parseInt(addClient_numTel.getText()),
                            addClient_dateNaiss.getValue(),
                            addClient_adresse.getText(),
                            addClient_email.getText(),
                            addClient_etatCivil.getValue(),
                            addClient_situationPro.getValue(),
                            Double.parseDouble(addClient_salaire.getText()),
                            addClient_patrimoine.getText(),
                            addClient_type.getValue(),
                            personneId
                    );

                    clientDAO.addClient(client);

                    loadClients();

                    clearField();
                    showInfo("Client ajouté avec succès !");
                } catch (SQLException e) {
                    showError("Erreur lors de l'ajout du client : " + e.getMessage());
                } catch (NumberFormatException e) {
                    showError("Erreur : certains champs numériques ne sont pas valides.");
                }
            }
        }

    @FXML
    private void deleteSelectedItem() throws SQLException {
        Client selectedClient = addClient_tableView.getSelectionModel().getSelectedItem();
        int id = selectedClient.getIdClient();
        ClientDAO.deleteClient(id);
        clientList.remove(selectedClient);
    }

    @FXML
    public void miseajourClient(){
        addClient_tableView.setEditable(true);

        addClient_col_prenom.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_prenom.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setPrenom(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Prénom modifié et mis à jour.");
        });

        addClient_col_nom.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_nom.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setNom(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Nom modifié et mis à jour.");
        });


        addClient_col_numTel.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_numTel.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setNumTel(Integer.parseInt(event.getNewValue()));
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Numéro de téléphone modifié et mis à jour.");
        });

        addClient_col_dateNaiss.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_dateNaiss.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setDateNaiss(LocalDate.parse(event.getNewValue()));
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Date de naissance modifiée et mise à jour.");
        });

        addClient_col_adresse.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_adresse.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setAdresse(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Adresse modifiée et mise à jour.");
        });

        addClient_col_email.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_email.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setEmail(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Email modifié et mis à jour.");
        });

        addClient_col_etatCivil.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_etatCivil.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setEtatCivil(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Etat civil modifié et mis à jour.");
        });

        addClient_col_salaire.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        addClient_col_salaire.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setSalaire(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Salaire modifié et mis à jour.");
        });

        addClient_col_situationPro.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_situationPro.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setSituationPro(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Situation Pro modifié et mis à jour.");
        });

        addClient_col_patrimoine.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_patrimoine.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setPatrimoineImmo(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Patrimoine modifié et mis à jour.");
        });

        addClient_col_type.setCellFactory(TextFieldTableCell.forTableColumn());
        addClient_col_type.setOnEditCommit(event -> {
            Client client = event.getRowValue();
            client.setType(event.getNewValue());
            try {
                ClientDAO.miseajourClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Le type est modifié et mis à jour.");
        });

    }

    @FXML
    private void clearField() {
        addClient_prenom.clear();
        addClient_nom.clear();
        addClient_numTel.clear();
        addClient_dateNaiss.setValue(null);
        addClient_adresse.clear();
        addClient_email.clear();
        addClient_salaire.clear();
        addClient_patrimoine.clear();
        addClient_etatCivil.getSelectionModel().clearSelection();
        addClient_situationPro.getSelectionModel().clearSelection();
        addClient_type.getSelectionModel().clearSelection();
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

    private boolean isdecoPaneVisible = false;
    private boolean islabelcomptePaneVisible = false;

    @FXML
    public void onlabelComptePane1Click(javafx.scene.input.MouseEvent event) {
        islabelcomptePaneVisible = !islabelcomptePaneVisible;
        isdecoPaneVisible = !isdecoPaneVisible;
        labelComptePane1.setVisible(!islabelcomptePaneVisible);
        labelComptePane2.setVisible(islabelcomptePaneVisible);
        decoPane.setVisible(isdecoPaneVisible);
    }

    public void onlabelComptePane2Click(javafx.scene.input.MouseEvent event) {
        islabelcomptePaneVisible = !islabelcomptePaneVisible;
        isdecoPaneVisible = !isdecoPaneVisible;
        labelComptePane1.setVisible(!islabelcomptePaneVisible);
        labelComptePane2.setVisible(islabelcomptePaneVisible);
        decoPane.setVisible(isdecoPaneVisible);

    }

    public void ondecoPaneClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent loginView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo/hello-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView, 1100, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void onacceuilNavClick(javafx.scene.input.MouseEvent event) {


        double windowWidth = 1100;
        double windowHeight = 600;
        try {
            Parent loginView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo/landing-page.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView, windowWidth, windowHeight));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onbiensNavClick(javafx.scene.input.MouseEvent event) {

        double windowWidth = 1100;
        double windowHeight = 600;
        try {
            Parent loginView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo/recherche-page.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView, windowWidth, windowHeight));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onemployeNavClick(javafx.scene.input.MouseEvent event) {
        if (!HelloController.admin) {
            showError("Vous n'avez pas les droits necessaire");

        } else {

            double windowWidth = 1100;
            double windowHeight = 600;
            try {
                Parent loginView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo/gestionEmploye.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(loginView, windowWidth, windowHeight));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }
}