package com.example.taskmanagement.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.taskmanagement.dao.DBconnection;

public class User {
    public static int user_id;
    private String username;
    private String password;
    private String email;
    private String first_name;
    private String last_name;
    Connection connectDB;
    List userList;
    public User(){

    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public List loadUserData() {
        String sql = "SELECT * FROM User"; // Replace 'Users' with your actual table name
        userList = new List();
        connectDB = DBconnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connectDB.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(username, password);
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

}
