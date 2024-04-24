package com.example.jdbcassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    private Label txtMessage;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField passPassword;

    @FXML
    protected void onSignupClick() {
        String user = txtUsername.getText();
        String pass = passPassword.getText();
        if (!user.equals("") && !pass.equals("")) {
            try (
                Connection c = MySQLConnection.getConnection(); /*automatically close()*/
                PreparedStatement statement = c.prepareStatement(
                        "INSERT INTO users (username, password) VALUES (?, ?);"
                );
            ) {
                statement.setString(1, user);
                statement.setString(2, pass);
                int rowsInserted = statement.executeUpdate();
                System.out.println("Rows Inserted: " + rowsInserted);
                txtMessage.setTextFill(Color.GREEN);
                txtMessage.setText("Signup successful!");
            } catch (SQLException e) {
                txtMessage.setTextFill(Color.RED);
                txtMessage.setText("Signup unsuccessful.");
                e.printStackTrace();
            }
        } else {
            txtMessage.setTextFill(Color.RED);
            txtMessage.setText("Signup unsuccessful.");
        }
    }

    @FXML
    protected void onLoginClick(ActionEvent event) {
        String user = txtUsername.getText();
        String pass = passPassword.getText();
        try (
                Connection c = MySQLConnection.getConnection();
                Statement statement = c.createStatement();
        ) {
            if (user.equals("") || pass.equals("")) {
                txtMessage.setTextFill(Color.RED);
                txtMessage.setText("Login unsuccessful.");
            }
            String query = "SELECT * FROM users WHERE username = '" + user + "';";
            ResultSet res = statement.executeQuery(query);
            if (res.next()) {
                if (res.getString("password").matches(pass)) {
                    txtMessage.setTextFill(Color.GREEN);
                    txtMessage.setText("Login successful!");

                    LoggedInUser.username = user;
                    LoggedInUser.id = res.getInt("id");
                    LoggedInUser.entrycount = res.getInt("entrycount");

                    txtUsername.setText("");
                    passPassword.setText("");
                    txtMessage.setText("");

                    Stage stage = HelloApplication.stage;
                    FXMLLoader fxml = new FXMLLoader(getClass().getResource("login-view.fxml"));
                    Parent p = fxml.load();
                    Scene s = new Scene(p);
                    LoginController.prevScene = ((Node)event.getSource()).getScene();
                    LoginController lc = fxml.getController();
                    lc.loadPage();
                    stage.setScene(s);
                    stage.show();

                } else {
                    txtMessage.setTextFill(Color.RED);
                    txtMessage.setText("Login unsuccessful.");
                }
            }
        } catch (Exception e) {
            txtMessage.setTextFill(Color.RED);
            txtMessage.setText("Login unsuccessful.");
            e.printStackTrace();
        }
    }
}