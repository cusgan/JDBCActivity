package com.example.jdbcassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class LoginController {
    public static Scene prevScene;
    @FXML
    private Text txtJournal;
    @FXML
    private Label txtEntryCount;
    @FXML
    private Button btnDeleteAcc;
    @FXML
    private ScrollPane spEntries;
    public void loadPage() {
        txtJournal.setText(LoggedInUser.username + "'s Journal");
        txtEntryCount.setText(LoggedInUser.entrycount + " Entries");

        WriteController.lc = this;
        VBox mainVbox = new VBox();
        mainVbox.setSpacing(25);
        ArrayList<Entry> entries = getEntries();
        for (int i = entries.size()-1; i >= 0; i--) {
            mainVbox.getChildren().add(buildEntry(entries.get(i)));
        }
        spEntries.setContent(mainVbox);
    }

    private Node buildEntry(Entry entry){
        VBox vbox = new VBox();
        Label txtDate = new Label(entry.dateposted.toString());
        Label txtTitle = new Label(entry.title);
        txtTitle.setFont(Font.font("System Bold", FontWeight.BOLD, 24));
        Label txtContent = new Label(entry.content);
        txtContent.setPrefWidth(580.0);
        txtContent.setWrapText(true);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(38.0);
        hbox.setPrefWidth(580.0);
        Button btnEdit = new Button("Edit Entry");
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //System.out.println("EDITED ENTRY #" + entry.entryid);
                try {
                    Stage stage = HelloApplication.stage;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("write-view.fxml"));
                    Parent p = loader.load();
                    Scene s = new Scene(p);
                    WriteController wc = loader.getController();
                    WriteController.entry = entry;
                    wc.loadPage();
                    WriteController.prevScene = ((Node)actionEvent.getSource()).getScene();
                    stage.setScene(s);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        btnEdit.setMnemonicParsing(false);
        Button btnDelete = new Button("Delete Entry");
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //System.out.println("DELETED ENTRY #" + entry.entryid);
                Connection c = MySQLConnection.getConnection();
                try (
                        PreparedStatement pst = c.prepareStatement(
                                "DELETE from entries where entryid = ?;");
                        PreparedStatement st = c.prepareStatement(
                                "UPDATE users SET entrycount = ? WHERE id = ?;"
                        );
                ){
                    c.setAutoCommit(false);
                    pst.setInt(1, entry.entryid);
                    st.setInt(1, --LoggedInUser.entrycount);
                    st.setInt(2, LoggedInUser.id);
                    pst.executeUpdate();
                    st.executeUpdate();

                    c.commit();
                    loadPage();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
//        btnDelete.setMnemonicParsing(false);

        hbox.getChildren().add(btnEdit);
        hbox.getChildren().add(btnDelete);
        vbox.getChildren().add(txtDate);
        vbox.getChildren().add(txtTitle);
        vbox.getChildren().add(txtContent);
        vbox.getChildren().add(hbox);

        return vbox;
    }

    private static ArrayList<Entry> getEntries() {
        ArrayList<Entry> entries = new ArrayList<>();

        try (
                Connection c = MySQLConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT * from entries WHERE userid = ?");
        ) {
            ps.setInt(1, LoggedInUser.id);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                Entry e = new Entry();
                e.entryid = res.getInt("entryid");
                e.dateposted = res.getTimestamp("dateposted");
                e.title = res.getString("title");
                e.content = res.getString("content");
                entries.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }

    @FXML
    protected void onNewEntryClick(ActionEvent event) {
        try {
            Stage stage = HelloApplication.stage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("write-view.fxml"));
            Parent p = loader.load();
            Scene s = new Scene(p);
            WriteController wc = loader.getController();
            WriteController.entry = null;
            wc.loadPage();
            WriteController.prevScene = ((Node)event.getSource()).getScene();
            stage.setScene(s);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteAccClick(ActionEvent e) {
        Connection c = MySQLConnection.getConnection();
        try {
            String query = "DELETE from users where id = ?;";
            PreparedStatement pst = c.prepareStatement(query);
            pst.setInt(1, LoggedInUser.id);
            pst.executeUpdate();
            pst.close();
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(prevScene);
            stage.show();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
//        TilePane r = new TilePane();
//        Alert a = new Alert(Alert.AlertType.NONE);
//        a.setAlertType(Alert.AlertType.CONFIRMATION);
//        a.setTitle("Confirm Account Deletion");
//        a.setHeaderText(null);
//        a.setContentText("Are you sure you would like to delete your account, " + LoggedInUser.username + "?\nThis action cannot be undone.");
//        //a.show();
//        System.out.println(a.getResult().getText());
//        Optional <ButtonType> action = null;
//        try{ action = a.showAndWait();
//        } catch (Exception ea){}
//        Connection c = MySQLConnection.getConnection();
//        if (action.get() == ButtonType.OK) {
//            try {
//                String query = "DELETE from users where id = ?;";
//                PreparedStatement pst = c.prepareStatement(query);
//                pst.setInt(1, LoggedInUser.id);
//                pst.executeUpdate();
//                pst.close();
//                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//                stage.setScene(prevScene);
//                stage.show();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        EventHandler<ActionEvent> event = new
//                EventHandler<ActionEvent>() {
//                    public void handle(ActionEvent e)
//                    {
//                        a.setAlertType(Alert.AlertType.CONFIRMATION);
//                        a.setTitle("Confirm Account Deletion");
//                        a.setHeaderText(null);
//                        a.setContentText("Are you sure you would like to delete your account, " + LoggedInUser.username + "?\nThis action cannot be undone.");
//                        a.show();
//                        Optional <ButtonType> action = a.showAndWait();
//                        Connection c = MySQLConnection.getConnection();
//                        if (action.get() == ButtonType.OK) {
//                            try {
//                                String query = "DELETE from users where id = ?;";
//                                PreparedStatement pst = c.prepareStatement(query);
//                                pst.setInt(1, LoggedInUser.id);
//                                pst.executeUpdate();
//                                pst.close();
//                                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//                                stage.setScene(prevScene);
//                                stage.show();
//                            } catch (SQLException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//                };
//
//        btnDeleteAcc.setOnAction(event);
    }

    @FXML
    protected void onLogoutClick() {
        Stage stage = HelloApplication.stage;
        stage.setScene(prevScene);
        stage.show();
    }
}
