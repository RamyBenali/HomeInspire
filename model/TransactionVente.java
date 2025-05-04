package com.example.demo.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class TransactionVente {
    private IntegerProperty idTransac;
    private ObjectProperty<LocalDate> dateTransac;
    private DoubleProperty montant;
    private StringProperty typeTransac;
    private IntegerProperty idBien;
    private IntegerProperty idPersonne;


    public TransactionVente(int idTransac, LocalDate dateTransac, double montant, String typeTransac, int idBien, int idPersonne) {
        this.idTransac = new SimpleIntegerProperty(idTransac);
        this.dateTransac = new SimpleObjectProperty<>(dateTransac);
        this.montant = new SimpleDoubleProperty(montant);
        this.typeTransac = new SimpleStringProperty(typeTransac);
        this.idBien = new SimpleIntegerProperty(idBien);
        this.idPersonne = new SimpleIntegerProperty(idPersonne);
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
}
