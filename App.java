import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class App extends Application {
    //text fields for staff information
    private TextField tfID = new TextField();
    private TextField tfLastName = new TextField();
    private TextField tfFirstName = new TextField();
    private TextField tfMI = new TextField();
    private TextField tfAddress = new TextField();
    private TextField tfCity = new TextField();
    private TextField tfState = new TextField();
    private TextField tfTelephone = new TextField();
    private TextField tfEmail = new TextField();

    private Connection connection; // JDBC connection

    @Override
    public void start(Stage primaryStage) {
        connectToDatabase(); // Connect to the database

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10));
        pane.setVgap(5);
        pane.setHgap(5);

        // Add labels and text fields to the pane
        pane.add(new Label("ID:"), 0, 0);
        pane.add(tfID, 1, 0);
        pane.add(new Label("Last Name:"), 0, 1);
        pane.add(tfLastName, 1, 1);
        pane.add(new Label("First Name:"), 0, 2);
        pane.add(tfFirstName, 1, 2);
        pane.add(new Label("MI:"), 0, 3);
        pane.add(tfMI, 1, 3);
        pane.add(new Label("Address:"), 0, 4);
        pane.add(tfAddress, 1, 4);
        pane.add(new Label("City:"), 0, 5);
        pane.add(tfCity, 1, 5);
        pane.add(new Label("State:"), 0, 6);
        pane.add(tfState, 1, 6);
        pane.add(new Label("Telephone:"), 0, 7);
        pane.add(tfTelephone, 1, 7);
        pane.add(new Label("Email:"), 0, 8);
        pane.add(tfEmail, 1, 8);

        // Create and add buttons
        Button btnView = new Button("View");
        Button btnInsert = new Button("Insert");
        Button btnUpdate = new Button("Update");

        pane.add(btnView, 0, 9);
        pane.add(btnInsert, 1, 9);
        pane.add(btnUpdate, 2, 9);

        // Event handlers for buttons
        btnView.setOnAction(e -> viewRecord());
        btnInsert.setOnAction(e -> insertRecord());
        btnUpdate.setOnAction(e -> updateRecord());

        // Set up and show the scene
        Scene scene = new Scene(pane, 500, 400);
        primaryStage.setTitle("Staff Database");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Connect to the SQLite database
    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:staff.db");

            // Create the Staff table
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Staff (" +
                "id CHAR(9) PRIMARY KEY," +
                "lastName VARCHAR(15)," +
                "firstName VARCHAR(15)," +
                "mi CHAR(1)," +
                "address VARCHAR(20)," +
                "city VARCHAR(20)," +
                "state CHAR(2)," +
                "telephone CHAR(10)," +
                "email VARCHAR(40))"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View a record based on the ID entered
    private void viewRecord() {
        try {
            String sql = "SELECT * FROM Staff WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, tfID.getText());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // If a record is found
                tfLastName.setText(rs.getString("lastName"));
                tfFirstName.setText(rs.getString("firstName"));
                tfMI.setText(rs.getString("mi"));
                tfAddress.setText(rs.getString("address"));
                tfCity.setText(rs.getString("city"));
                tfState.setText(rs.getString("state"));
                tfTelephone.setText(rs.getString("telephone"));
                tfEmail.setText(rs.getString("email"));
            } else {
                // No record found
                showAlert(Alert.AlertType.INFORMATION, "Record not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a record into the Staff table
    private void insertRecord() {
        try {
            String sql = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, tfID.getText());
            pstmt.setString(2, tfLastName.getText());
            pstmt.setString(3, tfFirstName.getText());
            pstmt.setString(4, tfMI.getText());
            pstmt.setString(5, tfAddress.getText());
            pstmt.setString(6, tfCity.getText());
            pstmt.setString(7, tfState.getText());
            pstmt.setString(8, tfTelephone.getText());
            pstmt.setString(9, tfEmail.getText());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Record inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error inserting record. Maybe ID already exists?");
        }
    }

    // Update an existing record based on ID
    private void updateRecord() {
        try {
            String sql = "UPDATE Staff SET lastName=?, firstName=?, mi=?, address=?, city=?, state=?, telephone=?, email=? WHERE id=?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, tfLastName.getText());
            pstmt.setString(2, tfFirstName.getText());
            pstmt.setString(3, tfMI.getText());
            pstmt.setString(4, tfAddress.getText());
            pstmt.setString(5, tfCity.getText());
            pstmt.setString(6, tfState.getText());
            pstmt.setString(7, tfTelephone.getText());
            pstmt.setString(8, tfEmail.getText());
            pstmt.setString(9, tfID.getText());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Record updated successfully!");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No record found with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error updating record.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
