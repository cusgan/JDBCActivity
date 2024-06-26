package com.example.jdbcassignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/dbmyjournal";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    static Connection getConnection() {
        Connection c = null;
        try {
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            //System.out.println("DB Connection Success!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void main(String[] args) {
        Connection c = getConnection();
        try {
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
