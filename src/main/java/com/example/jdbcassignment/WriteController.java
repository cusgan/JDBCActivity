package com.example.jdbcassignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class WriteController {
    public static Scene prevScene;
    @FXML
    private Label txtEntryMessage;
    @FXML
    private TextField txtEntryTitle;
    @FXML
    private TextArea txtContent;

    @FXML
    protected void onReturnClick() {
        Stage stage = HelloApplication.stage;
        stage.setScene(prevScene);
        stage.show();
    }

    @FXML
    protected void onPostEntryClick() {
        txtEntryMessage.setTextFill(Color.GREEN);
        txtEntryMessage.setText("Entry posted successfully!");
    }
}
