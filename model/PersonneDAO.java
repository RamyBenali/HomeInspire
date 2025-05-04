package com.example.demo.model;

import java.sql.*;

public class PersonneDAO {
    private Connection connection;

    public PersonneDAO(Connection connection) {
        this.connection = connection;
    }

    public int addPersonne(Personne personne) throws SQLException {
        String query = "INSERT INTO personne (nom, prenom, numTel, dateNaiss, adresse) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, personne.getNom());
            preparedStatement.setString(2, personne.getPrenom());
            preparedStatement.setInt(3, personne.getNumTel());
            preparedStatement.setDate(4, Date.valueOf(personne.getDateNaiss()));
            preparedStatement.setString(5, personne.getAdresse());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Insertion de la personne échouée, aucun ID généré.");
                }
            }
        }
    }

}
