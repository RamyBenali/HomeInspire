    package com.example.demo.controller;

    import com.example.demo.model.*;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.collections.transformation.FilteredList;
    import javafx.collections.transformation.SortedList;
    import javafx.fxml.*;
    import javafx.scene.Node;
    import javafx.scene.Scene;
    import javafx.scene.chart.BarChart;
    import javafx.scene.chart.XYChart;
    import javafx.scene.control.*;
    import javafx.scene.image.Image;
    import javafx.scene.layout.*;
    import javafx.stage.*;

    import java.awt.event.ActionEvent;
    import java.io.IOException;
    import java.sql.*;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.Objects;
    
    import javafx.scene.Parent;
    
    public class LandingPageController {
    
        @FXML
        public Pane comptePane1;
        @FXML
        public Pane decoPane;
        @FXML
        public Label labelComptePane1;
        @FXML
        public Label labelComptePane2;
        @FXML
        private Label biensNav;
        @FXML
        public Label employeNav;
        @FXML
        private Label clientNav;
        @FXML
        private Button decoBtn;
        @FXML
        private AnchorPane home_form;
        @FXML
        private Label home_totalEmployees1;
        @FXML
        private Label home_totalPresents1;
        @FXML
        private Label home_totalInactiveEm1;
        @FXML
        private BarChart<?, ?> home_chart1;
        @FXML
        private Button landing_transacBtn;
        @FXML
        private TableView<Transaction> transac_tableView;
        @FXML
        private TableColumn<Transaction, Number> transac_col_id;
        @FXML
        private TableColumn<Transaction, String> transac_col_date;
        @FXML
        private TableColumn<Transaction, Double> transac_col_montant;
        @FXML
        private TableColumn<Transaction, Number> transac_col_idBien;
        @FXML
        private TableColumn<Transaction, Number> transac_col_idClient;
        @FXML
        private TableColumn<Transaction, String> transac_col_type;
        @FXML
        private TableColumn<Transaction, String> transac_col_dateDebut;
        @FXML
        private TableColumn<Transaction, String> transac_col_dateFin;
        @FXML
        private TableView<TransactionVente> transac_tableView1;
        @FXML
        private TableColumn<TransactionVente, Number> transac_col_id1;
        @FXML
        private TableColumn<TransactionVente, String> transac_col_date1;
        @FXML
        private TableColumn<TransactionVente, Double> transac_col_montant1;
        @FXML
        private TableColumn<TransactionVente, Number> transac_col_idBien1;
        @FXML
        private TableColumn<TransactionVente, Number> transac_col_idClient1;
        @FXML
        private TableColumn<TransactionVente, String> transac_col_type1;
        @FXML
        private Button transac_retour;
        @FXML
        private Button transac_detail;
        @FXML
        private Pane transactionPane;
        @FXML
        private Pane landingPane;
        @FXML
        private TextField transac_recherche;
        @FXML
        private TextField transac_recherche1;

        private Connection connect;
        private Statement statement;
        private PreparedStatement prepare;
        private ResultSet result;
        private Image image;
        private double x = 0.0;
        private double y = 0.0;
        private Transaction transaction;
        private ObservableList<Transaction> transacList;
        private ObservableList<TransactionVente> transacVenteList;

        private TransactionDAO transactionDAO;
        private boolean isdecoPaneVisible = false;
        private boolean islabelcomptePaneVisible = false;

        @FXML
        public void initialize(){
            Connection connection = DatabaseConnection.connect();
            transactionDAO = new TransactionDAO(connection);
            transacList = FXCollections.observableArrayList();
            transacVenteList = FXCollections.observableArrayList();
            homeTotalEmployees();
            homeEmployeeTotalPresent();
            homeTotalInactive();
            homeChart();
            loadTransac();
            configureTable();


            labelComptePane1.setText(HelloController.employeeFullName);
            labelComptePane2.setText(HelloController.employeeFullName);
        }

        public void homeChart() {
            this.home_chart1.getData().clear();
            String sql = "SELECT dateTransac, COUNT(_idTransac) FROM transaction GROUP BY dateTransac ORDER BY TIMESTAMP(dateTransac) ASC LIMIT 7";
            this.connect = DatabaseConnection.connect();

            try {
                XYChart.Series chart = new XYChart.Series();
                this.prepare = this.connect.prepareStatement(sql);
                this.result = this.prepare.executeQuery();

                while(this.result.next()) {
                    chart.getData().add(new XYChart.Data(this.result.getString(1), this.result.getInt(2)));
                }

                this.home_chart1.getData().add(chart);
            } catch (Exception var3) {
                Exception e = var3;
                e.printStackTrace();
            }

        }

        public void homeTotalEmployees() {
            String sql = "SELECT COUNT(_idBien) FROM bienimmo";
            this.connect = DatabaseConnection.connect();
            int countData = 0;

            try {
                this.prepare = this.connect.prepareStatement(sql);

                for (this.result = this.prepare.executeQuery(); this.result.next(); countData = this.result.getInt("COUNT(_idBien)")) {
                }

                this.home_totalEmployees1.setText(String.valueOf(countData));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        public void homeEmployeeTotalPresent() {
            String sql = "SELECT COUNT(_idBien) FROM bienimmo WHERE dispo = 'Oui'";
            this.connect = DatabaseConnection.connect();
            int countData = 0;

            try {
                this.statement = this.connect.createStatement();

                for(this.result = this.statement.executeQuery(sql); this.result.next(); countData = this.result.getInt("COUNT(_idBien)")) {
                }

                this.home_totalPresents1.setText(String.valueOf(countData));
            } catch (Exception var4) {
                Exception e = var4;
                e.printStackTrace();
            }

        }

        public void homeTotalInactive() {
            String sql = "SELECT COUNT(_idTransac) FROM transaction WHERE typeTransac = 'Location'";
            this.connect = DatabaseConnection.connect();
            int countData = 0;

            try {
                this.prepare = this.connect.prepareStatement(sql);

                for(this.result = this.prepare.executeQuery(); this.result.next(); countData = this.result.getInt("COUNT(_idTransac)")) {
                }

                this.home_totalInactiveEm1.setText(String.valueOf(countData));
            } catch (Exception var4) {
                Exception e = var4;
                e.printStackTrace();
            }

        }

        @FXML
        private void ontransacBtnClick(javafx.event.ActionEvent actionEvent) {
            landingPane.setVisible(false);
            transactionPane.setVisible(true);
        }

        @FXML
        private void onRetourTransacBtnClick(javafx.event.ActionEvent actionEvent) {
            landingPane.setVisible(true);
            transactionPane.setVisible(false);
        }

        @FXML
        private void configureTable(){
            transac_col_id.setCellValueFactory(cellData -> cellData.getValue().IdTransacProperty());
            transac_col_date.setCellValueFactory(cellData -> {
                    LocalDate dateNaiss = cellData.getValue().getDateTransac();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new SimpleStringProperty(dateNaiss != null ? dateNaiss.format(formatter) : "");
             });
            transac_col_montant.setCellValueFactory(cellData -> cellData.getValue().montantProperty().asObject());
            transac_col_type.setCellValueFactory(cellData -> cellData.getValue().TypeTransacProperty());
            transac_col_idBien.setCellValueFactory(cellData -> cellData.getValue().IdBienProperty());
            transac_col_idClient.setCellValueFactory(cellData -> cellData.getValue().idPersonneProperty());
            transac_col_dateDebut.setCellValueFactory(cellData -> {
                    LocalDate dateDebut = cellData.getValue().getDateDebut();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return new SimpleStringProperty(dateDebut != null ? dateDebut.format(formatter) : "");
            });
            transac_col_dateFin.setCellValueFactory(cellData -> {
                LocalDate dateFin = cellData.getValue().getDateDebut();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return new SimpleStringProperty(dateFin != null ? dateFin.format(formatter) : "");
            });
            transac_tableView.setItems(transacList);

            transac_col_id1.setCellValueFactory(cellData -> cellData.getValue().IdTransacProperty());
            transac_col_date1.setCellValueFactory(cellData -> {
                LocalDate dateNaiss = cellData.getValue().getDateTransac();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return new SimpleStringProperty(dateNaiss != null ? dateNaiss.format(formatter) : "");
            });
            transac_col_montant1.setCellValueFactory(cellData -> cellData.getValue().montantProperty().asObject());
            transac_col_type1.setCellValueFactory(cellData -> cellData.getValue().TypeTransacProperty());
            transac_col_idBien1.setCellValueFactory(cellData -> cellData.getValue().IdBienProperty());
            transac_col_idClient1.setCellValueFactory(cellData -> cellData.getValue().idPersonneProperty());

            transac_tableView1.setItems(transacVenteList);
        }

        private void loadTransac() {
            try {
                transacList.clear();
                transacList.addAll(transactionDAO.getLocTransac());
            } catch (SQLException e) {
                showError("Erreur lors du chargement des transaction Location : " + e.getMessage());
            }
            try {
                transacVenteList.clear();
                transacVenteList.addAll(transactionDAO.getVenteTransac());
            } catch (SQLException e) {
                showError("Erreur lors du chargement des transactions Vente : " + e.getMessage());
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
        private void showError(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(message);
            alert.show();
        }

        @FXML
        private void onAcceuilBtnClick(javafx.scene.input.MouseEvent event) {
            landingPane.setVisible(true);
            transactionPane.setVisible(false);
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
