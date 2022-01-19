package Controllers;

import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import static Controllers.Helper.throwAlert;

public class AppointmentController {
    @FXML private TextField edit_location;
    @FXML private ChoiceBox<Contact> edit_contact;
    @FXML private ChoiceBox<Customer> edit_customer;
    @FXML private ChoiceBox<User> edit_user;
    @FXML private DatePicker edit_start;
    @FXML private DatePicker edit_end;
    @FXML private TextField edit_title;
    @FXML private TextField edit_type;
    @FXML private TextField edit_description;
    @FXML private Label id_label;
    @FXML private TextField edit_id;
    @FXML private Label contact_label;
    @FXML private Label description_label;
    @FXML private Label location_label;
    @FXML private Label customer_label;
    @FXML private Label start_label;
    @FXML private Label end_label;
    @FXML private Label title_label;
    @FXML private Label type_label;
    @FXML private Label user_label;
    @FXML private TableColumn<Appointment, Integer>  id;
    @FXML private TableColumn<Appointment, String> updated_by;
    @FXML private TableColumn<Appointment, LocalDate>  last_update;
    @FXML private TableColumn<Appointment, String>  created_by;
    @FXML private TableColumn<Appointment, LocalDate>  create_date;
    @FXML private TableColumn<Appointment, String>  description;
    @FXML private TableColumn<Appointment, LocalDate>  end_time;
    @FXML private TableColumn<Appointment, LocalDate>  start_time;
    @FXML private TableColumn<Appointment, String>  location_string;
    @FXML private TableColumn<Appointment, String>  title;
    @FXML private TableColumn<Appointment, String>  type;
    @FXML private TableColumn<Appointment, Integer>  user_id;
    @FXML private TableColumn<Appointment, String>  customer;

    @FXML private TableView<Appointment> appointment_table;
    @FXML private Button modify_button;
    @FXML private Button add_button;
    @FXML private Button delete_button;

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    private static Appointment selectedAppointment;
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
            description.setText(text.getString("description"));
            end_time.setText(text.getString("end_time"));
            start_time.setText(text.getString("start_time"));
            location_string.setText(text.getString("location"));
            title.setText(text.getString("title"));
            type.setText(text.getString("type"));
            user_id.setText(text.getString("user_id"));
            customer.setText(text.getString("customer_name"));
            add_button.setText(text.getString("add"));
            modify_button.setText(text.getString("modify"));
            delete_button.setText(text.getString("delete"));
        }
    }

    private void getAppointments() {
        allAppointments.clear();
        String query = "select * from appointments";
        try (Statement stmt = Helper.con.createStatement()) {
            rs = stmt.executeQuery(query);
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

    public void addAppointment() throws IOException {
        System.out.println("add appointment");
        Parent addAppointmentPage = FXMLLoader.load(getClass().getResource("../Views/appointment_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(addAppointmentPage));
        stage.show();
    }

    public void modifyAppointment() throws IOException {
        Appointment appointment = appointment_table.getSelectionModel().getSelectedItem();
        setSelectedAppointment(appointment);
        if (customer == null) { throwAlert("Error: No selected appointment", "Must select appointment to modify"); return; }
        Parent addAppointmentPage = FXMLLoader.load(getClass().getResource("../Views/appointment_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(addAppointmentPage));
        stage.show();
    }

    public void createAppointment() {
//        String address = edit_address.getText();
//        String name = edit_name.getText();
//        Division division = findDivision(edit_division.getValue());
//        String postal_code = edit_postal_code.getText();
//        String phone = edit_phone.getText();
        Date current_date = new Date(System.currentTimeMillis());
        String current_user = UserController.user.getUserName();
        // String.format("u1=%s;u2=%s;u3=%s;u4=%s;", u1, u2, u3, u4);
//        String query = String.format("INSERT into customers " +
//                "(Address, Create_Date, Created_By, Customer_Name, Division_ID, Last_Update, Last_Updated_By, Phone, Postal_Code)" +
//                " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", address, current_date, current_user, name, division.getId(), current_date, current_user, phone, postal_code);
//        try (Statement stmt = Helper.con.createStatement()) {
//            if (stmt.executeUpdate(query) == 1) {
//                getAppointments();
//                close();
//            } else {
//                throwAlert("Something went wrong", "Something went wrong");
//            }
//        } catch (SQLException throwable) {
//            throwable.printStackTrace();
//        }
    }

    public void updateAppointment() {
//        int id = Integer.parseInt(edit_id.getText());
//        String address = edit_address.getText();
//        String name = edit_name.getText();
//        Division division = findDivision(edit_division.getValue());
//        String postal_code = edit_postal_code.getText();
//        String phone = edit_phone.getText();
//        String query = String.format(
//                "UPDATE customers SET Address = '%s', Customer_Name = '%s', Division_ID = '%s', Phone = '%s', Postal_Code = '%s', Last_Update = '%s'," +
//                        " Last_Updated_By = '%s' WHERE Customer_ID = '%s';",
//                address, name, division.getId(), phone, postal_code, new Date(System.currentTimeMillis()), UserController.user.getUserName(), id);
//        try (Statement stmt = Helper.con.createStatement()) {
//            if (stmt.executeUpdate(query) == 1) {
//                getAppointments();
//                close();
//            } else {
//                throwAlert("Something went wrong", "Something went wrong");
//            }
//        } catch (SQLException throwable) {
//            throwable.printStackTrace();
//        }
    }

    public void deleteAppointment(MouseEvent mouseEvent) {
        Appointment appointment = appointment_table.getSelectionModel().getSelectedItem();
        setSelectedAppointment(appointment);
        if (customer == null) { throwAlert("Error: No selected appointment", "Must select appointment to delete"); return; }
        int id = selectedAppointment.getId();
        String query = String.format("DELETE from appointments WHERE Appointment_ID = '%s';", id);
        try (Statement stmt = Helper.con.createStatement()) {
            if (stmt.executeUpdate(query) == 1) {
                getAppointments();
            } else {
                throwAlert("Something went wrong", "Something went wrong");
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * @param appointment the selected appointment in appointment table
     */
    public static void setSelectedAppointment(Appointment appointment){
        selectedAppointment = appointment;
    }

    /**
     * closes scene
     */
    public void close() {
        Stage stage;
        if (appointment_table != null) {
            stage = (Stage) appointment_table.getScene().getWindow();
        } else {
            stage = (Stage) appointment_table.getScene().getWindow();
        }
        stage.close();
    }
}




