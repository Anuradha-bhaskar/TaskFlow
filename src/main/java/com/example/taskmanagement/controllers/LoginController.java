package com.example.taskmanagement.controllers;

import com.example.taskmanagement.Utils.List;
import com.example.taskmanagement.Utils.User;
import com.example.taskmanagement.dao.DBconnection;
import com.example.taskmanagement.Utils.SwitchScenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;


public class LoginController  {
    @FXML
    public Button loginButton;
    @FXML
    public Label loginMessageLabel;
    @FXML
    public TextField userNameTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Button registerButton;
    @FXML
    public AnchorPane loginPage;

    private String username;

    Connection connectDB;


    @FXML
    public void loginButtonOnAction(ActionEvent e){
        if(userNameTextField.getText().isBlank() && passwordTextField.getText().isBlank()){
            loginMessageLabel.setText("please enter username and password");
        }
        else if(userNameTextField.getText().isBlank()){
            loginMessageLabel.setText("please enter username");
        }
        else if(passwordTextField.getText().isBlank()){
            loginMessageLabel.setText("please enter password");
        }
        else {
            validateLogin(e);
        }
    }

    @FXML
    public void registerButtonOnClick(){
        createAccount();
    }

    public void validateLogin(ActionEvent e){
        connectDB = DBconnection.getConnection();
        username = userNameTextField.getText();
        String password = passwordTextField.getText();
        User checkUser = new User(username,password);
        User user = new User();
        List userList = user.loadUserData();

        boolean isValidLogin = userList.contains(checkUser);
        if(isValidLogin){
            User.user_id = getUserId(username);
            loginMessageLabel.setStyle("-fx-text-fill: green");
            loginMessageLabel.setText("Login Successful!!");
            //close login page
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.close();


            Parent parent = null;
            try {
                //open dashboard
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pages/dashboard.fxml")));
                Scene scene = new Scene(parent);
                Stage dashboard = new Stage();
                dashboard.setScene(scene);
                dashboard.setTitle("Dashboard");
                dashboard.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        else{
            loginMessageLabel.setText("Invalid login. Please Try again");
        }
    }


    public void createAccount(){
        try {
            new SwitchScenes(loginPage, "/pages/register.fxml");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getUserId(String userName){
        connectDB = DBconnection.getConnection();
        String query = "SELECT user_id FROM User WHERE username = ?";
        int user_id = 0;
        try {
            PreparedStatement pst = connectDB.prepareStatement(query);
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                user_id = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user_id;
    }
}
