package Controllers;

import Models.Appointment;
import Models.Division;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserController {
    @FXML
    private TextField id_field;
    @FXML
    private TextField password_field;
    @FXML
    private Label user_label;
    @FXML
    private Label password_label;
    @FXML
    private Button submit_button;
    @FXML
    private Label location_label;

    public static ObservableList<User> allUsers = FXCollections.observableArrayList();
    public static User user;
    String user_id;
    String password;
    ResourceBundle text;
    ResultSet rs;

    public void initialize() {
        Helper.connectToAndQueryDatabase();
        Locale currentLocale = Locale.getDefault();
        text = ResourceBundle.getBundle("TextBundle", currentLocale);
        location_label.setText(currentLocale.getDisplayLanguage() + ", " + currentLocale.getDisplayCountry());
        user_label.setText(text.getString("user_id"));
        password_label.setText(text.getString("password"));
        submit_button.setText(text.getString("submit"));
    }

    public void findUser() throws IOException {
        user_id = id_field.getText();
        password = password_field.getText();
        String query = "SELECT * FROM users WHERE User_id = '" + user_id + "' AND Password = '" + password + "';";
        try (Statement stmt = Helper.con.createStatement()) {
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                user = new User(
                        rs.getInt("User_Id"),
                        rs.getString("User_Name"),
                        rs.getTimestamp("Create_Date").toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update").toLocalDateTime(),
                        rs.getString("Last_Updated_By"),
                        rs.getString("Password"));
            } else {
                Helper.throwAlert(text.getString("login_error_1"), text.getString("login_error_2"), "");
            }

        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (Exception e) { /* Ignored */ }
            try {
                Helper.con.close();
            } catch (Exception e) { /* Ignored */ }
            Parent dashboard = FXMLLoader.load(getClass().getResource("../Views/dashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(dashboard));
            stage.show();
        }

    }

    public static void getAllUsers() {
        allUsers.clear();
        String query = "SELECT * FROM users;";
        try (Statement stmt = Helper.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                user = new User(
                    rs.getInt("User_Id"),
                    rs.getString("User_Name"),
                    rs.getTimestamp("Create_Date").toLocalDateTime(),
                    rs.getString("Created_By"),
                    rs.getTimestamp("Last_Update").toLocalDateTime(),
                    rs.getString("Last_Updated_By"),
                    rs.getString("Password"));
                allUsers.add(user);
            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
    }

    public static User getUser(int id) {
        User match = null;
        for (User d : allUsers) {
            if (d.getId() == (id)) {
                match = d;
            }
        };
        return match;
    }


    public static ObservableList<String> getAllUserNames() {
        ObservableList<String> allUserNames = FXCollections.observableArrayList();
        allUsers.forEach(user -> allUserNames.add(user.getUserName()));
        return allUserNames;
    }
}
