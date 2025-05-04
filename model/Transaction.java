package com.example.demo.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Transaction {
    private IntegerProperty idTransac;
    private ObjectProperty<LocalDate> dateTransac;
    private DoubleProperty montant;
    private StringProperty typeTransac;
    private IntegerProperty idBien;
    private IntegerProperty idPersonne;
    private ObjectProperty<LocalDate> dateDebut;
    private ObjectProperty<LocalDate> dateFin;


    public Transaction(int idTransac, LocalDate dateTransac, double montant, String typeTransac, int idBien, int idPersonne, LocalDate dateDebut, LocalDate dateFin) {
        this.idTransac = new SimpleIntegerProperty(idTransac);
        this.dateTransac = new SimpleObjectProperty<>(dateTransac);
        this.montant = new SimpleDoubleProperty(montant);
        this.typeTransac = new SimpleStringProperty(typeTransac);
        this.idBien = new SimpleIntegerProperty(idBien);
        this.idPersonne = new SimpleIntegerProperty(idPersonne);
        this.dateDebut = new SimpleObjectProperty<>(dateDebut);
        this.dateFin = new SimpleObjectProperty<>(dateFin);
    }

    public int getIdTransac() {
        return idTransac.get();
    }

    public void setIdTransac(int idTransac) {
        this.idTransac.set(idTransac);
    }

    public IntegerProperty IdTransacProperty() { return idTransac; }

    public LocalDate getDateTransac() {
        return dateTransac.get();
    }

    public void setDateTransac(LocalDate dateTransac) {
        this.dateTransac.set(dateTransac);
    }

    public ObjectProperty<LocalDate> DateTransacProperty() { return dateTransac; }

    public DoubleProperty montantProperty() {return montant; }

    public double getMontant() {return montant.get();}

    public void setMontant(double montant) {
        this.montant.set(montant);
    }

    public String getTypeTransac() {
        return typeTransac.get();
    }

    public void setTypeTransac(String typeTransac) {
        this.typeTransac.set(typeTransac);
    }

    public StringProperty TypeTransacProperty() { return typeTransac; }

    public int getIdBien() {
        return idBien.get();
    }

    public void setIdBien(int idBien) {
        this.idBien.set(idBien);
    }

    public IntegerProperty IdBienProperty() { return idBien; }

    public IntegerProperty idPersonneProperty() { return idPersonne; }

    public int getIdPersonne() {
        return idPersonne.get();
    }

    public void setIdPersonne(int idPersonne) {
        this.idPersonne.set(idPersonne);
    }

    public ObjectProperty<LocalDate> DateDebutProperty() { return dateDebut; }

    public ObjectProperty<LocalDate> DateFinProperty() { return dateFin; }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut.set(dateDebut);
    }

    public LocalDate getDateDebut() {
        return dateDebut.get();
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin.set(dateFin);
    }

    public LocalDate getDateFin() {
        return dateFin.get();
    }
}
