package com.example.jdbcassignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import java.sql.*;

public class WriteController {
    public static Scene prevScene;
    public static Entry entry;
    public static LoginController lc;
    @FXML
    private Label txtEntryMessage;
    @FXML
    private TextField txtEntryTitle;
    @FXML
    private TextArea txtContent;

    private void publishEdit() {
        String title = txtEntryTitle.getText();
        String content = txtContent.getText();

        if (!title.equals("") && !content.equals("")) {
            try (
                    Connection c = MySQLConnection.getConnection(); /*automatically close()*/
                    PreparedStatement statement = c.prepareStatement(
                            "UPDATE entries SET title = ?, content = ?, dateposted = ? WHERE entryid = ?;"
                    );
            ) {
                Date date = new java.util.Date();
                Timestamp ts = new Timestamp(date.getTime());
                statement.setString(1, title);
                statement.setString(2, content);
                statement.setTimestamp(3, ts);
                statement.setInt(4, entry.entryid);

                statement.executeUpdate();

                txtEntryMessage.setTextFill(Color.GREEN);
                txtEntryMessage.setText("Entry updated successfully!");

            } catch (SQLException e) {
                txtEntryMessage.setTextFill(Color.RED);
                txtEntryMessage.setText("Entry could not be updated.");
                e.printStackTrace();
            }
        } else {
            txtEntryMessage.setTextFill(Color.RED);
            txtEntryMessage.setText("Title/Content cannot be empty!");
        }
    }

    public void loadPage() {
        if(entry != null) {
            txtEntryTitle.setText(entry.title);
            txtContent.setText(entry.content);
        } else {
            txtEntryTitle.setText("");
            txtContent.setText("");
        }

    }

    @FXML
    protected void onReturnClick() {
        Stage stage = HelloApplication.stage;
        lc.loadPage();
        stage.setScene(prevScene);
        stage.show();
    }

    @FXML
    protected void onPostEntryClick() {
        if (entry != null) {
            publishEdit();
            return;
        }

        String title = txtEntryTitle.getText();
        String content = txtContent.getText();

        if (!title.equals("") && !content.equals("")) {
            try (
                    Connection c = MySQLConnection.getConnection(); /*automatically close()*/
                    PreparedStatement statement = c.prepareStatement(
                            "INSERT INTO entries (dateposted, title, content, userid) VALUES (?, ?, ?, ?);"
                    );
                    PreparedStatement st = c.prepareStatement(
                            "UPDATE users SET entrycount = ? WHERE id = ?;"
                    );
            ) {
                c.setAutoCommit(false);

                Date date = new java.util.Date();
                Timestamp ts = new Timestamp(date.getTime());
                statement.setTimestamp(1, ts);
                statement.setString(2, title);
                statement.setString(3, content);
                statement.setInt(4, LoggedInUser.id);

                st.setInt(1, ++LoggedInUser.entrycount);
                st.setInt(2, LoggedInUser.id);

                int rowsInserted = statement.executeUpdate();
                System.out.println("(Entries) Rows Inserted: " + rowsInserted);
                int rowsInserted2 = st.executeUpdate();
                System.out.println("(Users) Rows Updated: " + rowsInserted2);

                c.commit();

                txtEntryMessage.setTextFill(Color.GREEN);
                txtEntryMessage.setText("Entry posted successfully!");

            } catch (SQLException e) {
                txtEntryMessage.setTextFill(Color.RED);
                txtEntryMessage.setText("Entry could not be posted.");
                e.printStackTrace();
            }
        } else {
            txtEntryMessage.setTextFill(Color.RED);
            txtEntryMessage.setText("Title/Content cannot be empty!");
        }
    }

}
