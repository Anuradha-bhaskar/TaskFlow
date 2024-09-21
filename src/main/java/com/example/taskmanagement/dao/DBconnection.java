package com.example.taskmanagement.dao;

import java.sql.*;

public class DBconnection {
    public static Connection con;

    public static Connection getConnection() {
        String dburl = "jdbc:mysql://localhost:3306/TaskManagement"; // Database name : TaskManagement
        String dbuser = "root";
        String dbpass = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(dburl, dbuser, dbpass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;

    }

}
