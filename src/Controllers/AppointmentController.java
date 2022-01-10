package Controllers;

import Models.Appointment;
import Models.Customer;
import Models.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentController {
    @FXML private TableColumn<Appointment, String>  id;
    @FXML private TableColumn<Appointment, String> updated_by;
    @FXML private TableColumn<Appointment, String>  last_update;
    @FXML private TableColumn<Appointment, String>  created_by;
    @FXML private TableColumn<Appointment, String>  create_date;
    @FXML private TableView<Appointment> appointment_table;
    @FXML private Button modify_button;
    @FXML private Button add_button;
    @FXML private Button delete_button;

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    ResourceBundle text;
    ResultSet rs;


    public void initialize() {
        Helper.connectToAndQueryDatabase();
        getAppointments();
        appointment_table.setItems(allAppointments);
        setLocalDefault();
    }

    private void setLocalDefault() {
        Locale currentLocale = Locale.getDefault();
        text = ResourceBundle.getBundle("TextBundle", currentLocale);
        if(appointment_table != null) {
            id.setText(text.getString("id"));
            create_date.setText(text.getString("create_date"));
            created_by.setText(text.getString("created_by"));
            last_update.setText(text.getString("last_update"));
            updated_by.setText(text.getString("last_updated_by"));
            add_button.setText(text.getString("add"));
            modify_button.setText(text.getString("modify"));
            delete_button.setText(text.getString("delete"));
        }
    }

    private void getAppointments() {
        String query = "select * from appointments";
        try (Statement stmt = Helper.con.createStatement()) {
            rs = stmt.executeQuery(query);

//    Customer customer;
//    String description;
//    LocalDate end_time;
//    LocalDate start_time;
//    String location;
//    String title;
//    String type;
//    User user;
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("Appointment_ID"),
                    rs.getDate("Create_Date").toLocalDate(),
                    rs.getString("Created_By"),
                    rs.getDate("Last_Update").toLocalDate(),
                    rs.getString("Last_Updated_By"),
                    CustomerController.findCustomer(rs.getInt("Customer_ID")),
                    rs.getString("Description"),
                    rs.getDate("End").toLocalDate(),
                    rs.getDate("Start").toLocalDate(),
                    rs.getString("Location"),
                    rs.getString("Title"),
                    rs.getString("Type"),
                    rs.getInt("User_Id")
                    );
                allAppointments.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void modifyAppointment() {
    }

    public void deleteAppointment(MouseEvent mouseEvent) {
    }

    public void addAppointment(MouseEvent mouseEvent) {
    }
}




