package com.example.demo.controller;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.example.demo.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class GestionEmployeController {
    @FXML
    private AnchorPane addEmployee_form;
    @FXML
    private TableView<Employe> addEmployee_tableView;
    @FXML
    private TableColumn<Employe, Number> addEmployee_col_id;
    @FXML
    private TableColumn<Employe, String> addEmployee_col_prenom;
    @FXML
    private TableColumn<Employe, String> addEmployee_col_nom;
    @FXML
    private TableColumn<Employe, String> addEmployee_col_numTel;
    @FXML
    private TableColumn<Employe, String> addEmployee_col_dateNaiss;
    @FXML
    private TableColumn<Employe, String> addEmployee_col_adresse;
    @FXML
    private TableColumn<Employe, String> addEmployee_col_motDePasse;
    @FXML
    private TableColumn<Employe, String> addEmployee_col_poste;
    @FXML
    private TableColumn<Employe, Double> addEmployee_col_salaire;
    @FXML
    private TableColumn<Employe, String> addEmployee_col_email;
    @FXML
    private TextField addEmployee_search;
    @FXML
    private Button addEmployee_addBtn;
    @FXML
    private Button addEmployee_updateBtn;
    @FXML
    private Button addEmployee_deleteBtn;
    @FXML
    private Button addEmployee_clearBtn;

    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    private String[] positionList = new String[]{"Marketer Coordinator", "Web Developer (Back End)", "Web Developer (Front End)", "App Developer"};
    private String[] listGender = new String[]{"Male", "Female", "Others"};

    @FXML
    public Pane comptePane11;
    @FXML
    public Pane decoPane1;
    @FXML
    public Label labelComptePane1;
    @FXML
    public Label labelComptePane2;
    @FXML
    public Label biensNav;
    @FXML
    public Label acceuilNav;
    @FXML
    private TextField addEmployee_prenom;
    @FXML
    private TextField addEmployee_nom;
    @FXML
    private TextField addEmployee_numTel;
    @FXML
    private DatePicker addEmployee_dateNaiss;
    @FXML
    private TextField addEmployee_adresse;
    @FXML
    private TextField addEmployee_motDePasse;
    @FXML
    private ChoiceBox<String> addEmployee_poste;
    @FXML
    private TextField addEmployee_salaire;
    @FXML
    private TextField addEmployee_email;
    @FXML
    private Label employeNav;

    private PersonneDAO personneDAO;
    private EmployeDAO employeDAO;

    private ObservableList<Employe> employeList;

    public void initialize() {
        labelComptePane1.setText(HelloController.employeeFullName);
        labelComptePane2.setText(HelloController.employeeFullName);
        addEmployee_poste.getItems().addAll("Stagiaire", "Salarié", "PDG", "Responsable");
        addEmployee_poste.setValue("Stagiaire");
        Connection connection = DatabaseConnection.connect();
        personneDAO = new PersonneDAO(connection);
            employeDAO= new EmployeDAO(connection);
            employeList = FXCollections.observableArrayList();
            configureTable();
            loadEmployes();
            setupSearchFeature(addEmployee_tableView, employeList, addEmployee_search);
            miseajourEmploye();
    }

    public void setupSearchFeature(TableView<Employe> tableView, ObservableList<Employe> employeList, TextField addEmployee_search) {
        FilteredList<Employe> filteredData = new FilteredList<>(employeList, p -> true);

        addEmployee_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employe -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (employe.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employe.getPrenom().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(employe.getNumTel()).contains(lowerCaseFilter)) {
                    return true;
                } else if (employe.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employe.getPoste().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Employe> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }


    private void configureTable() {
        addEmployee_col_id.setCellValueFactory(cellData -> cellData.getValue().idEmployeProperty());
        addEmployee_col_nom.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        addEmployee_col_prenom.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        addEmployee_col_numTel.setCellValueFactory(cellData -> {
            Integer numTel = cellData.getValue().getNumTel();
            return new SimpleStringProperty(numTel != null ? numTel.toString() : "");
        });
        addEmployee_col_dateNaiss.setCellValueFactory(cellData -> {
            LocalDate dateNaiss = cellData.getValue().getDateNaiss();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new SimpleStringProperty(dateNaiss != null ? dateNaiss.format(formatter) : "");
        });
        addEmployee_col_adresse.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
        addEmployee_col_motDePasse.setCellValueFactory(cellData -> cellData.getValue().motDePasseProperty());
        addEmployee_col_poste.setCellValueFactory(cellData -> cellData.getValue().posteProperty());
        addEmployee_col_salaire.setCellValueFactory(cellData -> cellData.getValue().salaireProperty().asObject());
        addEmployee_col_email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        addEmployee_tableView.setItems(employeList);
    }

    private void loadEmployes() {
        try {
            employeList.clear();
            employeList.addAll(employeDAO.getAllEmployes());
        } catch (SQLException e) {
            showError("Erreur lors du chargement des clients : " + e.getMessage());
        }
    }


    @FXML
    private void deleteSelectedItem() throws SQLException {
        Employe selectedEmploye = addEmployee_tableView.getSelectionModel().getSelectedItem();
        int id = selectedEmploye.getIdEmploye();
        EmployeDAO.deleteEmploye(id);
        employeList.remove(selectedEmploye);
    }

    @FXML
    public void miseajourEmploye(){
        addEmployee_tableView.setEditable(true);

        addEmployee_col_nom.setCellFactory(TextFieldTableCell.forTableColumn());
        addEmployee_col_nom.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setNom(event.getNewValue());
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Nom modifié et mis à jour.");
        });

        addEmployee_col_prenom.setCellFactory(TextFieldTableCell.forTableColumn());
        addEmployee_col_prenom.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setPrenom(event.getNewValue());
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Prénom modifié et mis à jour.");
        });

        addEmployee_col_numTel.setCellFactory(TextFieldTableCell.forTableColumn());
        addEmployee_col_numTel.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setNumTel(Integer.parseInt(event.getNewValue()));
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Numéro de téléphone modifié et mis à jour.");
        });

        addEmployee_col_dateNaiss.setCellFactory(TextFieldTableCell.forTableColumn());
        addEmployee_col_dateNaiss.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setDateNaiss(LocalDate.parse(event.getNewValue()));
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Date de naissance modifiée et mise à jour.");
        });

        addEmployee_col_adresse.setCellFactory(TextFieldTableCell.forTableColumn());
        addEmployee_col_adresse.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setAdresse(event.getNewValue());
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Adresse modifiée et mise à jour.");
        });

        addEmployee_col_motDePasse.setCellFactory(TextFieldTableCell.forTableColumn());
        addEmployee_col_motDePasse.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setMotDePasse(event.getNewValue());
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Mot de passe modifié et mis à jour.");
        });

        addEmployee_col_poste.setCellFactory(TextFieldTableCell.forTableColumn());
        addEmployee_col_poste.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setPoste(event.getNewValue());
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Poste modifié et mis à jour.");
        });

        addEmployee_col_salaire.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        addEmployee_col_salaire.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setSalaire(event.getNewValue());
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Salaire modifié et mis à jour.");
        });

        addEmployee_col_email.setCellFactory(TextFieldTableCell.forTableColumn());
        addEmployee_col_email.setOnEditCommit(event -> {
            Employe employe = event.getRowValue();
            employe.setEmail(event.getNewValue());
            try {
                EmployeDAO.miseajourEmploye(employe);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Email modifié et mis à jour.");
        });
    }

    @FXML
    private void addEmploye() {
        boolean verif = true;
        resetTF();
        if (!addEmployee_nom.getText().matches("^[a-zA-ZÀ-ÿ\\s'-]+$")) {
            showErrorTF(addEmployee_nom, verif);
        } else if (!addEmployee_prenom.getText().matches("^[a-zA-ZÀ-ÿ\\s'-]+$")){
            showErrorTF(addEmployee_prenom, verif);
        }else if (!addEmployee_numTel.getText().matches("^\\d{10}$")){
            showErrorTF(addEmployee_numTel, verif);
        }else if (!addEmployee_email.getText().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")){
            showErrorTF(addEmployee_email, verif);
        }else if(!addEmployee_motDePasse.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[@$!%*.?&#])[A-Za-z\\\\d@$!%*?&#]{8,}$")){
            addEmployee_motDePasse.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showError("Le mot de Passe doit contenir au moins 1 majuscule, 1 miniscule, 1 chiffre et 1 symbole");
            verif = false;
        }else if (!addEmployee_salaire.getText().matches("^(0|[1-9]\\d*)(\\.\\d{1,2})?$")){
            showErrorTF(addEmployee_salaire, verif);
        }else {
            try {
                // Créer une nouvelle personne
                Personne personne = new Personne(
                        addEmployee_nom.getText(),
                        addEmployee_prenom.getText(),
                        Integer.parseInt(addEmployee_numTel.getText()),
                        addEmployee_dateNaiss.getValue(),
                        addEmployee_adresse.getText()
                );

                int personneId = personneDAO.addPersonne(personne);

                Employe employe = new Employe(
                        personneId, // ID de la personne
                        addEmployee_nom.getText(),
                        addEmployee_prenom.getText(),
                        Integer.parseInt(addEmployee_numTel.getText()),
                        addEmployee_dateNaiss.getValue(),
                        addEmployee_adresse.getText(),
                        addEmployee_motDePasse.getText(),
                        addEmployee_poste.getValue(), // Récupérer la valeur de la ChoiceBox
                        Double.parseDouble(addEmployee_salaire.getText()),
                        personneId,
                        addEmployee_email.getText());

                employeDAO.ajouterEmploye(employe);

                loadEmployes();

                clearFields();
                showInfo("Employe ajouté avec succès !");
            } catch (SQLException e) {
                showError("Erreur lors de l'ajout de l'employe : " + e.getMessage());
            } catch (NumberFormatException e) {
                showError("Erreur : certains champs numériques ne sont pas valides.");
            }
        }
    }

    @FXML
    private void resetTF() {
        addEmployee_nom.setStyle(null);
        addEmployee_prenom.setStyle(null);
        addEmployee_numTel.setStyle(null);
        addEmployee_email.setStyle(null);
        addEmployee_salaire.setStyle(null);
        addEmployee_motDePasse.setStyle(null);
    }

    @FXML
    private void showErrorTF(TextField tf, boolean verif){
        tf.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        showError("Le champ "+tf.getPromptText()+" ne respecte pas la syntaxe !");
        verif = false;
    }


    @FXML
    private void clearFields() {
        addEmployee_nom.clear();
        addEmployee_prenom.clear();
        addEmployee_numTel.clear();
        addEmployee_dateNaiss.setValue(null);
        addEmployee_adresse.clear();
        addEmployee_motDePasse.clear();
        addEmployee_salaire.clear();
        addEmployee_email.clear();
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

    @FXML
    public void onclientNavClick(javafx.scene.input.MouseEvent event) {

        double windowWidth = 1100;
        double windowHeight = 600;
        try {
            Parent loginView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo/gestionClient-page.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView, windowWidth, windowHeight));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isdecoPaneVisible = false;
    private boolean islabelcomptePaneVisible = false;

    @FXML
    public void onlabelComptePane1Click(javafx.scene.input.MouseEvent event) {
        islabelcomptePaneVisible = !islabelcomptePaneVisible;
        isdecoPaneVisible = !isdecoPaneVisible;
        labelComptePane1.setVisible(!islabelcomptePaneVisible);
        labelComptePane2.setVisible(islabelcomptePaneVisible);
        decoPane1.setVisible(isdecoPaneVisible);
    }

    public void onlabelComptePane2Click(javafx.scene.input.MouseEvent event) {
        islabelcomptePaneVisible = !islabelcomptePaneVisible;
        isdecoPaneVisible = !isdecoPaneVisible;
        labelComptePane1.setVisible(!islabelcomptePaneVisible);
        labelComptePane2.setVisible(islabelcomptePaneVisible);
        decoPane1.setVisible(isdecoPaneVisible);

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


        // Définir une taille proportionnelle à l'écran (par exemple, 80% de la largeur et 80% de la hauteur)
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

