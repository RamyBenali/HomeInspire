package com.example.demo.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Date;

public class Personne {

    private StringProperty nom;
    private StringProperty prenom;
    private IntegerProperty numTel;
    private ObjectProperty<LocalDate> dateNaiss;
    private StringProperty adresse;

    public Personne(String nom, String prenom, int numTel, LocalDate dateNaiss, String adresse) {
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.numTel = new SimpleIntegerProperty(numTel);
        this.dateNaiss = new SimpleObjectProperty<>(dateNaiss);
        this.adresse = new SimpleStringProperty(adresse);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public int getNumTel() {
        return numTel.get();
    }

    public void setNumTel(int numTel) {
        this.numTel.set(numTel);
    }

    public IntegerProperty numTelProperty() {
        return numTel;
    }

    public LocalDate getDateNaiss() {
        return dateNaiss.get();
    }

    public void setDateNaiss(LocalDate dateNaiss) {
        this.dateNaiss.set(dateNaiss);
    }

    public ObjectProperty<LocalDate> dateNaissProperty() {
        return dateNaiss;
    }

    public String getAdresse() {
        return adresse.get();
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public StringProperty adresseProperty() {
        return adresse;
    }
}
