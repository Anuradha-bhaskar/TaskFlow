module com.example.taskmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.taskmanagement to javafx.fxml;
    exports com.example.taskmanagement;

    opens com.example.taskmanagement.controllers to javafx.fxml;
    exports com.example.taskmanagement.controllers;

    opens com.example.taskmanagement.dao to javafx.fxml;
    exports com.example.taskmanagement.dao;

    opens com.example.taskmanagement.Utils to javafx.fxml;
    exports com.example.taskmanagement.Utils;
}