package com.example.taskmanagement.controllers;

import com.example.taskmanagement.dao.DBconnection;
import com.example.taskmanagement.Utils.SwitchScenes;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {
    @FXML
    public Button registerButton;
    @FXML
    public Label registerMessageLabel;
    @FXML
    public TextField lastnameTextField;
    @FXML
    public TextField firstnameTextField;
    @FXML
    public TextField userNameTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public TextField emailTextField;
    @FXML
    public AnchorPane registerPage;


    String firstName;
    @FXML
    public void registerButtonOnAction(ActionEvent e){
        if(firstnameTextField.getText().isBlank() || lastnameTextField.getText().isBlank() || userNameTextField.getText().isBlank() ||
        passwordTextField.getText().isBlank() || emailTextField.getText().isBlank()){
            registerMessageLabel.setText("Please enter all the details");
        }
        else if(!validateEmail()){
            registerMessageLabel.setText("please enter a valid email address");
        }
        else if(checkUsername()){
            registerMessageLabel.setText("This username already exists.");
        }
        else if(!validateUsername()){
            registerMessageLabel.setText("Only a-z, 0-9, ., -, _ allowed in username");
        }
        else if(checkName(firstnameTextField.getText())){
            registerMessageLabel.setText("Name should only contains alphabets");
        }
        else if(checkName(lastnameTextField.getText())){
            registerMessageLabel.setText("Name should only contains alphabets");
        }
        else{
            addUser();
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> new SwitchScenes(registerPage, "/pages/login.fxml"));
            pause.play();

        }
    }

    public boolean checkUsername(){
        DBconnection db = new DBconnection();
        Connection connectDB = db.getConnection();
        String username = userNameTextField.getText();
        String verifyUsername = "SELECT COUNT(1) FROM User WHERE username = '" + username + "'";
        boolean usernameExists = false;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyUsername);
            while (queryResult.next()){
                usernameExists = queryResult.getInt(1) == 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usernameExists;
    }
    public boolean validateUsername(){
        String usernameRegex = "^[a-z0-9._-]+$" ;
        Pattern pattern = Pattern.compile(usernameRegex);
        Matcher matcher = pattern.matcher(userNameTextField.getText().trim());
        return matcher.matches();
    }
    public boolean validateEmail(){

        String emailRegex = "^[a-z0-9._$]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailTextField.getText().trim());
        return matcher.matches();
    }
    public boolean checkName(String name){
        String nameRegex = "^[a-zA-Z0-9._-]+$" ;
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name.trim());
        return !matcher.matches();
    }
    public void addUser(){
        DBconnection db = new DBconnection();
        Connection connectDB = db.getConnection();
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();
        String email = emailTextField.getText();
        firstName = firstnameTextField.getText();
        String lastName = lastnameTextField.getText();
        String createAccount = "INSERT INTO `User`(`username`, `password`, `email`, `first_name`, `last_name`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pst = connectDB.prepareStatement(createAccount);
            pst.setString(1,username);
            pst.setString(2,password);
            pst.setString(3,email);
            pst.setString(4,capitalize(firstName));
            pst.setString(5,capitalize(lastName));
            int r = pst.executeUpdate();
            if(r>0){
                registerMessageLabel.setStyle("-fx-text-fill: green");
                registerMessageLabel.setText("User Registered Successfully");
            }
            else{
                registerMessageLabel.setText("Please try again");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String capitalize(String str)
    {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
