package com.example.demo.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Employe extends Personne {
    private final IntegerProperty idEmploye;
    private final StringProperty motDePasse;
    private final StringProperty poste;
    private final DoubleProperty salaire;
    private final IntegerProperty idPersonne;
    private final StringProperty email;

    public Employe(int idEmploye, String nom, String prenom, int numTel, LocalDate dateNaiss, String adresse, String motDePasse, String poste, double salaire, int idPersonne, String email) {
        super(nom, prenom, numTel, dateNaiss, adresse);
        this.idEmploye = new SimpleIntegerProperty(idEmploye);
        this.motDePasse = new SimpleStringProperty(motDePasse);
        this.poste = new SimpleStringProperty(poste);
        this.salaire = new SimpleDoubleProperty(salaire);
        this.idPersonne = new SimpleIntegerProperty(idPersonne);
        this.email = new SimpleStringProperty(email);
    }

    public int getIdEmploye() {
        return idEmploye.get();
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye.set(idEmploye);
    }

    public IntegerProperty idEmployeProperty() {
        return idEmploye;
    }

    public String getMotDePasse() {
        return motDePasse.get();
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse.set(motDePasse);
    }

    public StringProperty motDePasseProperty() {
        return motDePasse;
    }

    public String getPoste() {
        return poste.get();
    }

    public void setPoste(String poste) {
        this.poste.set(poste);
    }

    public StringProperty posteProperty() {
        return poste;
    }

    public double getSalaire() {
        return salaire.get();
    }

    public void setSalaire(double salaire) {
        this.salaire.set(salaire);
    }

    public DoubleProperty salaireProperty() {
        return salaire;
    }

    public int getIdPersonne() {
        return idPersonne.get();
    }

    public void setIdPersonne(int idPersonne) {
        this.idPersonne.set(idPersonne);
    }

    public IntegerProperty idPersonneProperty() {
        return idPersonne;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }
}
