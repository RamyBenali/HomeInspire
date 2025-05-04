package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class recherchePageApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("recherche-page.fxml"));
        Parent root = fxmlLoader.load();

        double windowWidth = 1100;
        double windowHeight = 600;

        Scene scene = new Scene(root, windowWidth, windowHeight);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Home Inspire");
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}