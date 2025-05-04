package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class LandingPageApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("landing-page.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1100, 600);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Home Inspire");
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}