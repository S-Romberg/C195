package Controllers;

import java.io.File;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import static Controllers.Helper.throwAlert;

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
    static ResourceBundle text;
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
        String loginQuery = "SELECT * FROM users WHERE User_id = '" + user_id + "' AND Password = '" + password + "';";
        try (Statement stmt = Helper.con.createStatement()) {
            rs = stmt.executeQuery(loginQuery);
            if (rs.next()) {
                user = new User(
                        rs.getInt("User_Id"),
                        rs.getString("User_Name"),
                        rs.getTimestamp("Create_Date").toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update").toLocalDateTime(),
                        rs.getString("Last_Updated_By"),
                        rs.getString("Password"));
                recordLoginAttempt(true);
                appointmentAlert(user);
                Parent dashboard = FXMLLoader.load(getClass().getResource("../Views/dashboard.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(dashboard));
                stage.show();
            } else {
                recordLoginAttempt(false);
                Helper.throwAlert(text.getString("login_error_1"), text.getString("login_error_2"), "");
            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void appointmentAlert(User user) {
        int ID = 0;
        LocalDateTime start = null;
        String appointmentQuery = "SELECT * from appointments a where a.User_ID = '" + user.getId() + "' and start > now() and start < now() + INTERVAL 15 MINUTE;";
        try (Statement stmt = Helper.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(appointmentQuery);
            if (rs.next()) {
                ID = rs.getInt("Appointment_ID");
                start = rs.getTimestamp("Start").toLocalDateTime();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(ID != 0 && start != null) {
            throwAlert(text.getString("upcoming_appointment"), text.getString("upcoming_appointment") + ": " + ID + " " + start
                    , "");
        } else {
            throwAlert(text.getString("no_appointments"), text.getString("no_appointments"), "");
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

    public static void recordLoginAttempt(boolean successful) {
        try {
            String str = "\n Login attempt at: " + ZonedDateTime.now() + " Result: " + successful;
            // will create file if it doesn't exist
            File myObj = new File("login_activity.txt");
            myObj.createNewFile();

            Files.write(Paths.get("login_activity.txt"), str.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getAllUserNames() {
        ObservableList<String> allUserNames = FXCollections.observableArrayList();
        allUsers.forEach(user -> allUserNames.add(user.getUserName()));
        return allUserNames;
    }
}
