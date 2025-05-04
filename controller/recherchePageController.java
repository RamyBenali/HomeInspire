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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class recherchePageController {
    @FXML
    private AnchorPane main_form;
    @FXML
    private TextField addBien_search;
    @FXML
    private TextField addBien_adresse;
    @FXML
    private TextField addBien_prix;
    @FXML
    private ComboBox<String> addBien_dispo;
    @FXML
    private TextField addBien_proprio;
    @FXML
    private ComboBox<String> addBien_type;
    @FXML
    private TextField addBien_description;
    @FXML
    private ImageView addBien_image;
    @FXML
    private Button addBien_importBtn;
    @FXML
    private Button addBien_addBtn;
    @FXML
    private Button addBien_updateBtn;
    @FXML
    private Button addBien_deleteBtn;
    @FXML
    private Button addBien_clearBtn;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    private String[] positionList = new String[]{"Marketer Coordinator", "Web Developer (Back End)", "Web Developer (Front End)", "App Developer"};
    private String[] listGender = new String[]{"Male", "Female", "Others"};
    private double x = 0.0;
    private double y = 0.0;

    @FXML
    private TableView<BienImmo> addBien_tableView;
    @FXML
    private TableColumn<BienImmo, Number> addBien_col_bienID;
    @FXML
    private TableColumn<BienImmo, String> addBien_col_adresse;
    @FXML
    private TableColumn<BienImmo, Double> addBien_col_prix;
    @FXML
    private TableColumn<BienImmo, String> addBien_col_dispo;
    @FXML
    private TableColumn<BienImmo, String> addBien_col_description;
    @FXML
    private TableColumn<BienImmo, String> addBien_col_proprio;
    @FXML
    private TableColumn<BienImmo, String> addBien_col_type;

    @FXML
    public Pane comptePane1;
    @FXML
    public Pane decoPane1;
    @FXML
    public Label labelComptePane1;
    @FXML
    public Label labelComptePane2;

    @FXML
    private Label clientNav;

    @FXML
    private Label acceuilNav;

    @FXML
    private Label employeNav;

    private File selectedImageFile;

    private BienImmoDAO bienImmoDAO;
    private String ImagePath;


    @FXML
    private void onclientNavClick(javafx.scene.input.MouseEvent event) {

        try {
            Parent loginView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo/gestionClient-page.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView, 1100, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rafraichirTableau() throws SQLException {
        ObservableList<BienImmo> data = FXCollections.observableArrayList(
                bienImmoDAO.getAllBiens()
        );
        addBien_tableView.setItems(data);
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

    private ObservableList<BienImmo> biensData;

    @FXML
    public void initialize() {

        addBien_type.getItems().addAll("Appartement", "Maison", "Terrain", "Duplex", "Local", "Hangar");
        addBien_type.setValue("Appartement");
        addBien_dispo.getItems().addAll("Oui", "Non");
        addBien_dispo.setValue("Oui");
        labelComptePane1.setText(HelloController.employeeFullName);
        labelComptePane2.setText(HelloController.employeeFullName);
        Connection connection = DatabaseConnection.connect();
        bienImmoDAO = new BienImmoDAO(connection);
        biensData = FXCollections.observableArrayList();

        loadBiens();
        configureTable();
        setupSearchFeature(addBien_tableView, biensData, addBien_search);
        miseajourBien();
    }

    public void configureTable() {
        addBien_col_bienID.setCellValueFactory(cellData -> cellData.getValue().idBienProperty());
        addBien_col_adresse.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
        addBien_col_prix.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());
        addBien_col_dispo.setCellValueFactory(cellData -> cellData.getValue().dispoProperty());
        addBien_col_description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        addBien_col_proprio.setCellValueFactory(cellData -> cellData.getValue().proprietaireProperty());
        addBien_col_type.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        addBien_tableView.setItems(biensData);
    }


    public void loadBiens() {

        try {
            biensData.clear();
            biensData.addAll(bienImmoDAO.getAllBiens());
        } catch (SQLException e) {
            showError("Erreur lors du chargement des biens : " + e.getMessage());
        }
    }

    public void setupSearchFeature(TableView<BienImmo> tableView, ObservableList<BienImmo> bienList, TextField addBien_search) {
        FilteredList<BienImmo> filteredData = new FilteredList<>(bienList, p -> true);

        addBien_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(bienImmo -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (bienImmo.getAdresse().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(bienImmo.getPrix()).contains(lowerCaseFilter)) {
                    return true;
                } else if (bienImmo.getDispo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bienImmo.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bienImmo.getproprietaire().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bienImmo.getType().toLowerCase().contains(lowerCaseFilter)) {

                }

                return false;
            });
        });

        SortedList<BienImmo> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }

    @FXML
    public void addBienInsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(this.main_form.getScene().getWindow());
        if (file != null) {
            getData.path = file.getAbsolutePath();
            this.image = new Image(file.toURI().toString(), 182.0, 127.0, false, true);
            this.addBien_image.setImage(this.image);
        }

    }

    @FXML
    private void resetTF() {
        addBien_prix.setStyle(null);
        addBien_proprio.setStyle(null);
    }

    @FXML
    private void showErrorTF(TextField tf, boolean verif) {
        tf.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        showError("Le champ "+tf.getPromptText()+" ne respecte pas la syntaxe !");
        verif = false;
    }

    @FXML
    private void addBien() {
        boolean verif = true;
        int id = 1;
        String uri = getData.path;
        if (!addBien_prix.getText().matches("^(0|[1-9]\\d*)(\\.\\d{1,2})?$")) {
            showErrorTF(addBien_prix, verif);
        } else if (!addBien_proprio.getText().matches("^[A-ZÀ-Ÿ][a-zà-ÿ]+\\s[A-ZÀ-Ÿ][a-zà-ÿ]+$")) {
            showErrorTF(addBien_proprio, verif);
        } else {
            uri = uri.replace("\\", "\\\\");
            try {
                BienImmo bienImmo = new BienImmo(
                        id,
                        addBien_adresse.getText(),
                        Double.parseDouble(addBien_prix.getText()),
                        addBien_dispo.getValue().toString(),
                        addBien_description.getText(),
                        uri,
                        addBien_proprio.getText(),
                        addBien_type.getValue().toString());

                bienImmoDAO.addBien(bienImmo);
                loadBiens();
                clearFields();
                showInfo("Bien ajouté avec succès !");
            } catch (SQLException e) {
                showError("Erreur lors de l'ajout du bien : " + e.getMessage());
            } catch (NumberFormatException e) {
                showError("Erreur : certains champs numériques ne sont pas valides.");
            }
        }
    }

    @FXML
    public void miseajourBien(){
        addBien_tableView.setEditable(true);
        addBien_col_adresse.setCellFactory(TextFieldTableCell.forTableColumn());
        addBien_col_adresse.setOnEditCommit(event -> {
            BienImmo bienimmo = event.getRowValue();
            bienimmo.setAdresse(event.getNewValue());
            try {
                BienImmoDAO.miseajourBien(bienimmo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Adresse modifié et mis à jour.");
        });

        addBien_col_prix.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        addBien_col_prix.setOnEditCommit(event -> {
            BienImmo bienImmo = event.getRowValue();
            bienImmo.setPrix(event.getNewValue());
            try {
                BienImmoDAO.miseajourBien(bienImmo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Prix modifié et mis à jour.");
        });

        addBien_col_dispo.setCellFactory(TextFieldTableCell.forTableColumn());
        addBien_col_dispo.setOnEditCommit(event -> {
            BienImmo bienImmo = event.getRowValue();
            bienImmo.setDispo(event.getNewValue());
            try {
                BienImmoDAO.miseajourBien(bienImmo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("La disponnibilité modifié et mis à jour.");
        });

        addBien_col_description.setCellFactory(TextFieldTableCell.forTableColumn());
        addBien_col_description.setOnEditCommit(event -> {
            BienImmo bienImmo = event.getRowValue();
            bienImmo.setDescription(event.getNewValue());
            try {
                BienImmoDAO.miseajourBien(bienImmo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Date de naissance modifiée et mise à jour.");
        });

        addBien_col_proprio.setCellFactory(TextFieldTableCell.forTableColumn());
        addBien_col_proprio.setOnEditCommit(event -> {
            BienImmo bienImmo = event.getRowValue();
            bienImmo.setProprietaire(event.getNewValue());
            try {
                BienImmoDAO.miseajourBien(bienImmo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Adresse modifiée et mise à jour.");
        });

        addBien_col_type.setCellFactory(TextFieldTableCell.forTableColumn());
        addBien_col_type.setOnEditCommit(event -> {
            BienImmo bienImmo = event.getRowValue();
            bienImmo.setType(event.getNewValue());
            try {
                BienImmoDAO.miseajourBien(bienImmo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Mot de passe modifié et mis à jour.");
        });

    }

    @FXML
    public void onSelectItem() {
        BienImmo selectedBien = addBien_tableView.getSelectionModel().getSelectedItem();

        if (selectedBien != null) {
            if (Objects.equals(selectedBien.getDispo(), "Oui")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/BienDetails.fxml"));
                    Parent root = loader.load();

                    // Passer les informations au contrôleur de la nouvelle fenêtre
                    bienDetailsController controller = loader.getController();
                    controller.setBienDetails(selectedBien);

                    // Créer une nouvelle scène et une nouvelle fenêtre
                    Stage stage = new Stage();
                    stage.setTitle("Détails du Bien");
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Erreur lors du chargement de la fenêtre des détails du bien : " + e.getMessage());
                }
            }else {
                showError("Le bien sélectionné n'est pas disponnible");
            }
        } else {
                showError("Aucun bien n'as était selectionné !");
        }
    }


    @FXML
    private void deleteSelectedItem() throws SQLException {
        BienImmo selectedBien = addBien_tableView.getSelectionModel().getSelectedItem();
        int id = selectedBien.getIdBien();
        BienImmoDAO.deleteBien(id);
        biensData.remove(selectedBien);
    }

    public void clearFields() {
        this.addBien_adresse.setText("");
        this.addBien_prix.setText("");
        this.addBien_dispo.getSelectionModel().clearSelection();
        this.addBien_proprio.setText("");
        this.addBien_type.getSelectionModel().clearSelection();
        this.addBien_description.setText("");
        this.addBien_image.setImage((Image)null);
        getData.path = "";
    }

    private boolean isdecoPaneVisible = false;
    private boolean islabelcomptePaneVisible = false;

    @FXML
    public void onlabelComptePane1Click(MouseEvent event) {
        islabelcomptePaneVisible = !islabelcomptePaneVisible;
        isdecoPaneVisible = !isdecoPaneVisible;
        labelComptePane1.setVisible(!islabelcomptePaneVisible);
        labelComptePane2.setVisible(islabelcomptePaneVisible);
        decoPane1.setVisible(isdecoPaneVisible);
    }

    public void onlabelComptePane2Click(MouseEvent event) {
        islabelcomptePaneVisible = !islabelcomptePaneVisible;
        isdecoPaneVisible = !isdecoPaneVisible;
        labelComptePane1.setVisible(!islabelcomptePaneVisible);
        labelComptePane2.setVisible(islabelcomptePaneVisible);
        decoPane1.setVisible(isdecoPaneVisible);

    }
    public void ondecoPaneClick(MouseEvent event) {
        try {
            Parent loginView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo/hello-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView, 1100, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
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

