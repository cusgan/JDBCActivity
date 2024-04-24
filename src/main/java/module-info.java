module com.example.jdbcassignment {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.example.jdbcassignment to javafx.fxml;
    exports com.example.jdbcassignment;
}