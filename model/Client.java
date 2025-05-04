package com.example.demo.model;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Client extends Personne {

    private IntegerProperty idClient;
    private StringProperty email;
    private StringProperty etatCivil;
    private StringProperty situationPro;
    private DoubleProperty salaire;
    private StringProperty patrimoineImmo;
    private StringProperty type;
    private IntegerProperty idPers;

    public Client(int idClient, String nom, String prenom, int numTel, LocalDate dateNaiss, String adresse, String email,
                  String etatCivil, String situationPro, double salaire, String patrimoineImmo, String type, int idPers) {
        super(nom, prenom, numTel, dateNaiss, adresse);
        this.idClient = new SimpleIntegerProperty(idClient);
        this.email = new SimpleStringProperty(email);
        this.etatCivil = new SimpleStringProperty(etatCivil);
        this.situationPro = new SimpleStringProperty(situationPro);
        this.salaire = new SimpleDoubleProperty(salaire);
        this.patrimoineImmo = new SimpleStringProperty(patrimoineImmo);
        this.type = new SimpleStringProperty(type);
        this.idPers = new SimpleIntegerProperty(idPers);
    }

    public int getIdClient() {
        return idClient.get();
    }

    public void setIdClient(int idClient) {
        this.idClient.set(idClient);
    }

    public IntegerProperty idClientProperty() {
        return idClient;
    }

    // Getter et Setter pour email
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Idem pour les autres propriétés
    public String getEtatCivil() {
        return etatCivil.get();
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil.set(etatCivil);
    }

    public StringProperty etatCivilProperty() {
        return etatCivil;
    }

    public String getSituationPro() {
        return situationPro.get();
    }

    public void setSituationPro(String situationPro) {
        this.situationPro.set(situationPro);
    }

    public StringProperty situationProProperty() {
        return situationPro;
    }

    public Double getSalaire() {
        return salaire.get();
    }

    public void setSalaire(double salaire) {
        this.salaire.set(salaire);
    }

    public DoubleProperty salaireProperty() {
        return salaire;
    }

    public String getPatrimoineImmo() {
        return patrimoineImmo.get();
    }

    public void setPatrimoineImmo(String patrimoineImmo) {
        this.patrimoineImmo.set(patrimoineImmo);
    }

    public StringProperty patrimoineImmoProperty() {
        return patrimoineImmo;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getIdPers() {
        return idPers.get();
    }

    public void setIdPers(int idPers) {
        this.idPers.set(idPers);
    }

    public IntegerProperty idPersProperty() {
        return idPers;
    }
}
