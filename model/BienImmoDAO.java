package com.example.demo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BienImmoDAO {
    private final Connection connection;


    public BienImmoDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BienImmo> getAllBiens() throws SQLException {
        List<BienImmo> biens = new ArrayList<>();
        String query = "SELECT * FROM bienimmo";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                biens.add(new BienImmo(
                        resultSet.getInt("_idBien"),
                        resultSet.getString("adresse"),
                        resultSet.getDouble("prix"),
                        resultSet.getString("dispo"),
                        resultSet.getString("description"),
                        resultSet.getString("image"),
                        resultSet.getString("proprietaire"),
                        resultSet.getString("type")
                ));
            }
        }
        return biens;
    }

    public void addBien(BienImmo bien) throws SQLException {
        String query = "INSERT INTO bienimmo (adresse, prix, dispo, description, image, proprietaire, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement biensStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            biensStmt.setString(1, bien.getAdresse());
            biensStmt.setDouble(2, bien.getPrix());
            biensStmt.setString(3, bien.getDispo());
            biensStmt.setString(4, bien.getDescription());
            biensStmt.setString(5, bien.getImage());
            biensStmt.setString(6, bien.getproprietaire());
            biensStmt.setString(7, bien.getType());

            biensStmt.executeUpdate();
        }
    }

    public static void miseajourBien(BienImmo bien) throws SQLException {
        String updateBienQuery = """
        UPDATE bienimmo 
        SET adresse = ?, prix = ?, dispo = ?, description = ? , proprietaire = ?, type = ?
        WHERE _idBien = ?
    """;

        try (Connection connection = DatabaseConnection.connect();) {
            connection.setAutoCommit(false);

            try (PreparedStatement bienStmt = connection.prepareStatement(updateBienQuery)) {
                bienStmt.setString(1, bien.getAdresse());
                bienStmt.setDouble(2, bien.getPrix());
                bienStmt.setString(3, bien.getDispo());
                bienStmt.setString(4, bien.getDescription());
                bienStmt.setString(5, bien.getproprietaire());
                bienStmt.setString(6, bien.getType());
                bienStmt.setInt(7, bien.getIdBien());

                bienStmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Mise à jour réussie pour l'employé et la personne associée.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour des informations.");
        }
    }

    public static void updateDisponibilite(int idBien, String dispo) throws SQLException {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "UPDATE bienimmo SET dispo = ? WHERE _idBien = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, dispo);
                statement.setInt(2, idBien);
                statement.executeUpdate();
            }
        }
    }

    public static void deleteBien(int id) throws SQLException {

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "DELETE FROM bienimmo WHERE _idBien = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
