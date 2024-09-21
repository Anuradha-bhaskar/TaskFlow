package com.example.taskmanagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pages/login.fxml")));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("dashboard");
        stage.show();
    }

    public static void main(String[] args)  {
        launch();
    }
}
