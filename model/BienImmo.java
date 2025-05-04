package com.example.demo.model;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class BienImmo {
    private IntegerProperty idBien;
    private StringProperty adresse;
    private DoubleProperty prix;
    private StringProperty dispo;
    private StringProperty description;
    private StringProperty image;
    private StringProperty proprietaire;
    private StringProperty type;

    public BienImmo(int idBien, String adresse, double prix, String dispo, String description, String image, String proprietaire, String type) {
        this.idBien = new SimpleIntegerProperty(idBien);
        this.adresse = new SimpleStringProperty(adresse);
        this.prix = new SimpleDoubleProperty(prix);
        this.dispo = new SimpleStringProperty(dispo);
        this.description = new SimpleStringProperty(description);
        this.image = new SimpleStringProperty(image);
        this.proprietaire = new SimpleStringProperty(proprietaire);
        this.type = new SimpleStringProperty(type);
    }

    public int getIdBien() {
        return idBien.get();
    }
    public void setIdBien(int idBien) {
        this.idBien.set(idBien);
    }
    public IntegerProperty idBienProperty() {
        return idBien;
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


    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }
    public DoubleProperty prixProperty() {
        return prix;
    }
    public String getDispo() {
        return dispo.get();
    }

    public void setDispo(String dispo) {
        this.dispo.set(dispo);
    }

    public StringProperty dispoProperty() {
        return dispo;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getImage() {
        return image.get();
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    public StringProperty imageProperty() {
        return image;
    }

    public String getproprietaire() {
        return proprietaire.get();
    }

    public void setProprietaire(String idPers) {
        this.proprietaire.set(idPers);
    }

    public StringProperty proprietaireProperty() {
        return proprietaire;
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
}

