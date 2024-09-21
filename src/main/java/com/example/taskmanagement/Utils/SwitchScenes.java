package com.example.taskmanagement.Utils;

import com.example.taskmanagement.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class SwitchScenes {
    public SwitchScenes(AnchorPane currentAnchorpane, String fxml) {
        AnchorPane nextAnchorPane;
        try {
            nextAnchorPane = FXMLLoader.load(Objects.requireNonNull(App.class.getResource(fxml)));
            currentAnchorpane.getChildren().removeAll();
            currentAnchorpane.getChildren().setAll(nextAnchorPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
