package com.example.jdbcassignment;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void main() {
        Connection c = MySQLConnection.getConnection();
        String query1 = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "username VARCHAR(50) UNIQUE not null, " +
                "password VARCHAR(50) not null, " +
                "entrycount INT default 0)";
        String query2 = "CREATE TABLE IF NOT EXISTS entries (" +
                "entryid INT PRIMARY KEY AUTO_INCREMENT, " +
                "userid INT not null, " +
                "dateposted DATETIME not null, " +
                "title VARCHAR(100) not null, " +
                "content VARCHAR(500) not null, " +
                "FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE)";
        try {
            Statement statement = c.createStatement();
            statement.execute(query1);
            statement.execute(query2);
            System.out.println("Tables created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
