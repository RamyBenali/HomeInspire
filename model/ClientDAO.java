package com.example.demo.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private final Connection connection;

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client INNER JOIN personne ON client._idPers = personne._idPers";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                clients.add(new Client(
                        resultSet.getInt("_idClient"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getInt("numTel"),
                        resultSet.getDate("dateNaiss").toLocalDate(),
                        resultSet.getString("adresse"),
                        resultSet.getString("email"),
                        resultSet.getString("etatCivil"),
                        resultSet.getString("situationPro"),
                        resultSet.getDouble("salaire"),
                        resultSet.getString("patrimoineImmo"),
                        resultSet.getString("type"),
                        resultSet.getInt("_idPers")
                ));
            }
        }
        return clients;
    }

    public void addClient(Client client) throws SQLException {
        String personQuery = "INSERT INTO personne (nom, prenom, numTel, dateNaiss, adresse) VALUES (?, ?, ?, ?, ?)";
        String clientQuery = "INSERT INTO client (email, etatCivil, situationPro, salaire, patrimoineImmo, type, _idPers) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement personStmt = connection.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement clientStmt = connection.prepareStatement(clientQuery)) {

            // Ajouter la personne
            personStmt.setString(1, client.getNom());
            personStmt.setString(2, client.getPrenom());
            personStmt.setInt(3, client.getNumTel());
            personStmt.setDate(4, Date.valueOf(client.getDateNaiss()));
            personStmt.setString(5, client.getAdresse());
            personStmt.executeUpdate();

            ResultSet generatedKeys = personStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idPers = generatedKeys.getInt(1);

                // Ajouter le client
                clientStmt.setString(1, client.getEmail());
                clientStmt.setString(2, client.getEtatCivil());
                clientStmt.setString(3, client.getSituationPro());
                clientStmt.setDouble(4, client.getSalaire());
                clientStmt.setString(5, client.getPatrimoineImmo());
                clientStmt.setString(6, client.getType());
                clientStmt.setInt(7, idPers);
                clientStmt.executeUpdate();
            }
        }
    }

    public static void deleteClient(int id) throws SQLException {

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "DELETE FROM client WHERE _idClient = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void miseajourClient(Client client) throws SQLException {
        String updatePersonneQuery = """
        UPDATE personne 
        SET nom = ?, prenom = ?, numTel = ?, dateNaiss = ?, adresse = ? 
        WHERE _idPers = ?
    """;

        String updateClientQuery = """
        UPDATE client 
        SET email = ?, etatCivil = ?, situationPro = ?, salaire = ?, patrimoineImmo = ?, type = ?
        WHERE _idClient = ?
    """;

        try (Connection connection = DatabaseConnection.connect();) {
            connection.setAutoCommit(false);

            try (PreparedStatement personStmt = connection.prepareStatement(updatePersonneQuery)) {
                personStmt.setString(1, client.getNom());
                personStmt.setString(2, client.getPrenom());
                personStmt.setInt(3, client.getNumTel());
                personStmt.setDate(4, Date.valueOf(client.getDateNaiss())); // Assurez-vous que `dateNaiss` est une `LocalDate`
                personStmt.setString(5, client.getAdresse());
                personStmt.setInt(6, client.getIdPers());

                personStmt.executeUpdate();
            }

            try (PreparedStatement clientStmt = connection.prepareStatement(updateClientQuery)) {
                clientStmt.setString(1, client.getEmail());
                clientStmt.setString(2, client.getEtatCivil());
                clientStmt.setString(3, client.getSituationPro());
                clientStmt.setDouble(4, client.getSalaire());
                clientStmt.setString(5, client.getPatrimoineImmo());
                clientStmt.setString(6, client.getType());
                clientStmt.setInt(7, client.getIdClient());

                clientStmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Mise à jour réussie pour l'employé et la personne associée.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour des informations.");
        }
    }


}
