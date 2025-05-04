package com.example.demo.model;

import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {
    private final Connection connection;

    public EmployeDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Employe> getAllEmployes() throws SQLException {
        List<Employe> employes = new ArrayList<>();
        String query = "SELECT * FROM employe INNER JOIN personne ON employe._idPers = personne._idPers";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                employes.add(new Employe(
                        resultSet.getInt("_idEmploye"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getInt("numTel"),
                        resultSet.getDate("dateNaiss").toLocalDate(),
                        resultSet.getString("adresse"),
                        resultSet.getString("motDePasse"),
                        resultSet.getString("poste"),
                        resultSet.getDouble("salaire"),
                        resultSet.getInt("_idPers"),
                        resultSet.getString("email")
                ));
            }
        }
        return employes;
    }

    public void ajouterEmploye(Employe employe) throws SQLException {
        String personQuery = "INSERT INTO personne (nom, prenom, numTel, dateNaiss, adresse) VALUES (?, ?, ?, ?, ?)";
        String employeQuery = "INSERT INTO employe (motDePasse, poste, salaire, _idPers, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement personStmt = connection.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement employeStmt = connection.prepareStatement(employeQuery)) {

            personStmt.setString(1, employe.getNom());
            personStmt.setString(2, employe.getPrenom());
            personStmt.setInt(3, employe.getNumTel());
            personStmt.setDate(4, Date.valueOf(employe.getDateNaiss()));
            personStmt.setString(5, employe.getAdresse());
            personStmt.executeUpdate();

            ResultSet generatedKeys = personStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idPers = generatedKeys.getInt(1);

                employeStmt.setString(1, employe.getMotDePasse());
                employeStmt.setString(2, employe.getPoste());
                employeStmt.setDouble(3, employe.getSalaire());
                employeStmt.setInt(4, idPers);
                employeStmt.setString(5, employe.getEmail());
                employeStmt.executeUpdate();
            }
        }
    }

    public static void deleteEmploye(int id) throws SQLException {

            // Supprimer de la base de données
            try (Connection connection = DatabaseConnection.connect()) {
                String query = "DELETE FROM employe WHERE _idEmploye = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    public static void miseajourEmploye(Employe employe) throws SQLException {
        String updatePersonneQuery = """
        UPDATE personne 
        SET nom = ?, prenom = ?, numTel = ?, dateNaiss = ?, adresse = ? 
        WHERE _idPers = ?
    """;

        String updateEmployeQuery = """
        UPDATE employe 
        SET motDePasse = ?, poste = ?, salaire = ?, email = ? 
        WHERE _idEmploye = ?
    """;

        try (Connection connection = DatabaseConnection.connect();) {
            connection.setAutoCommit(false);

            try (PreparedStatement personStmt = connection.prepareStatement(updatePersonneQuery)) {
                personStmt.setString(1, employe.getNom());
                personStmt.setString(2, employe.getPrenom());
                personStmt.setInt(3, employe.getNumTel());
                personStmt.setDate(4, Date.valueOf(employe.getDateNaiss())); // Assurez-vous que `dateNaiss` est une `LocalDate`
                personStmt.setString(5, employe.getAdresse());
                personStmt.setInt(6, employe.getIdPersonne());

                personStmt.executeUpdate();
            }

            try (PreparedStatement employeStmt = connection.prepareStatement(updateEmployeQuery)) {
                employeStmt.setString(1, employe.getMotDePasse());
                employeStmt.setString(2, employe.getPoste());
                employeStmt.setDouble(3, employe.getSalaire());
                employeStmt.setString(4, employe.getEmail());
                employeStmt.setInt(5, employe.getIdEmploye());

                employeStmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Mise à jour réussie pour l'employé et la personne associée.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour des informations.");
        }
    }

    }


