    package com.example.demo.model;
    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;
    import com.example.demo.model.Transaction;
    import com.example.demo.model.TransactionVente;

    public class TransactionDAO {
        private final Connection connection;

        public TransactionDAO(Connection connection) {
            this.connection = connection;
        }



        public void ajouterTransaction(Transaction transaction) {
            String query;
            if (transaction.getTypeTransac().equals("Vente")){
                query = "INSERT INTO transaction (dateTransac, montant, typeTransac, _idBien, _idPers) VALUES (?, ?, ?, ?, ?)";
            }else{
                query = "INSERT INTO transaction (dateTransac, montant, typeTransac, _idBien, _idPers, dateDebutLocation, dateFinLocation) VALUES (?, ?, ?, ?, ?, ?, ?)";
            }
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setDate(1, Date.valueOf(transaction.getDateTransac()));
                stmt.setDouble(2, transaction.getMontant());
                stmt.setString(3, transaction.getTypeTransac());
                stmt.setInt(4, transaction.getIdBien());
                stmt.setInt(5, transaction.getIdPersonne());

                if (!transaction.getTypeTransac().equals("Vente")) {
                    stmt.setDate(6, Date.valueOf(transaction.getDateDebut()));
                    stmt.setDate(7, Date.valueOf(transaction.getDateFin()));
                }

                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de la transaction : " + e.getMessage());
            }
        }

        public List<Transaction> getLocTransac() throws SQLException {
            List<Transaction> transac = new ArrayList<>();
            String query = "SELECT * FROM transaction WHERE typeTransac = 'Location'";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    transac.add(new Transaction(
                            resultSet.getInt("_idTransac"),
                            resultSet.getDate("dateTransac").toLocalDate(),
                            resultSet.getDouble("montant"),
                            resultSet.getString("typeTransac"),
                            resultSet.getInt("_idBien"),
                            resultSet.getInt("_idPers"),
                            resultSet.getDate("dateDebutLocation").toLocalDate(),
                            resultSet.getDate("dateFinLocation").toLocalDate()
                    ));
                }
            }
            return transac;
        }

        public List<TransactionVente> getVenteTransac() throws SQLException {
            List<TransactionVente> transac = new ArrayList<>();
            String query = "SELECT _idTransac, dateTransac, montant, typeTransac, _idBien, _idPers FROM transaction WHERE typeTransac = 'Vente'";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    transac.add(new TransactionVente(
                            resultSet.getInt("_idTransac"),
                            resultSet.getDate("dateTransac").toLocalDate(),
                            resultSet.getDouble("montant"),
                            resultSet.getString("typeTransac"),
                            resultSet.getInt("_idBien"),
                            resultSet.getInt("_idPers")
                    ));
                }
            }
            return transac;
        }
    }
