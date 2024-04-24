package com.example.jdbcassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

public class LoginController {
    public static Scene prevScene;
    @FXML
    private Text txtJournal;
    @FXML
    private Label txtEntryCount;

    public void loadPage() {
        txtJournal.setText(LoggedInUser.username + "'s Journal");
        txtEntryCount.setText(LoggedInUser.entrycount + " Entries");
    }

    @FXML
    protected void onNewEntryClick(ActionEvent event) {
        try {
            Stage stage = HelloApplication.stage;
            Parent p = FXMLLoader.load(getClass().getResource("write-view.fxml"));
            Scene s = new Scene(p);
            WriteController.prevScene = ((Node)event.getSource()).getScene();
            stage.setScene(s);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLogoutClick() {
        Stage stage = HelloApplication.stage;
        stage.setScene(prevScene);
        stage.show();
    }
}
