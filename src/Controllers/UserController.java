package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    String user_id;
    String password;
    ResourceBundle text;

    public void initialize() {
        Controller.connectToAndQueryDatabase();
        Locale currentLocale = Locale.getDefault();
        text = ResourceBundle.getBundle("TextBundle", currentLocale);
        location_label.setText(currentLocale.getDisplayLanguage() + ", " + currentLocale.getDisplayCountry());
        user_label.setText(text.getString("user_id"));
        password_label.setText(text.getString("password"));
        submit_button.setText(text.getString("submit"));
    }

    public void findUser() {
        System.out.println("running findUser");
        user_id = id_field.getText();
        password = password_field.getText();
        System.out.println(user_id + password);
        String query = "SELECT * FROM users WHERE User_id = '" + user_id + "' AND Password = '" + password + "';";
        try (Statement stmt = Controller.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                while (rs.next()) {
                    String user_name = rs.getString("User_Name");
                    System.out.println("user name: " + user_name);
                }
            } else {
                Controller.throwAlert(text.getString("login_error_1"), text.getString("login_error_2"));
            }
            } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
