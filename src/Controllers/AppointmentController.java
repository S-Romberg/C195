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
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static Controllers.Helper.throwAlert;

public class AppointmentController {
    @FXML private TextField edit_location;
    @FXML private ChoiceBox<Contact> edit_contact;
    @FXML private ChoiceBox<Customer> edit_customer;
    @FXML private ChoiceBox<User> edit_user;
    @FXML private TextField edit_start;
    @FXML private TextField edit_end;
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
    @FXML private TableColumn<Appointment, LocalDateTime>  last_update;
    @FXML private TableColumn<Appointment, String>  created_by;
    @FXML private TableColumn<Appointment, LocalDateTime>  create_date;
    @FXML private TableColumn<Appointment, String>  description;
    @FXML private TableColumn<Appointment, LocalDateTime>  end_time;
    @FXML private TableColumn<Appointment, LocalDateTime>  start_time;
    @FXML private TableColumn<Appointment, String>  location_string;
    @FXML private TableColumn<Appointment, String>  title;
    @FXML private TableColumn<Appointment, String>  type;
    @FXML private TableColumn<Appointment, Integer>  user_id;
    @FXML private TableColumn<Appointment, String>  customer;

    @FXML private TableView<Appointment> appointment_table;
    @FXML private Button modify_button;
    @FXML private Button add_button;
    @FXML private Button delete_button;
    @FXML private Button save_button;
    @FXML private Button cancel_button;

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static Appointment selectedAppointment;
    ResourceBundle text;
    ResultSet rs;


    public void initialize() {
        setLocalDefault();
        Helper.connectToAndQueryDatabase();
        if (appointment_table != null) {
            getAppointments();
            appointment_table.setItems(allAppointments);
        } else if (edit_user != null){
            setFormValues();
        }
    }

    private void setFormValues() {
        UserController.getAllUsers();
        CustomerController.getCustomers();
        ContactController.getAllContacts();
        edit_user.setItems(UserController.allUsers);
        edit_customer.setItems(CustomerController.allCustomers);
        edit_contact.setItems(ContactController.allContacts);
        if (selectedAppointment != null) {
            edit_id.setText(String.valueOf(selectedAppointment.getId()));
            edit_location.setText(selectedAppointment.getLocationString());
            edit_start.setText(String.valueOf(selectedAppointment.getStartTime()));
            edit_end.setText(String.valueOf(selectedAppointment.getEndTime()));
            edit_title.setText(selectedAppointment.getTitle());
            edit_type.setText(selectedAppointment.getType());
            edit_description.setText(selectedAppointment.getDescription());
            edit_contact.setValue(selectedAppointment.getContact());
            edit_customer.setValue(selectedAppointment.getCustomer());
            edit_user.setValue(selectedAppointment.getUser());
            save_button.setOnMouseClicked(e -> updateAppointment());
        }
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
        } else {
            id_label.setText(text.getString("id"));
            description_label.setText(text.getString("description"));
            location_label.setText(text.getString("location"));
            contact_label.setText(text.getString("contact"));
            customer_label.setText(text.getString("customer"));
            user_label.setText(text.getString("user"));
            start_label.setText(text.getString("start_time"));
            end_label.setText(text.getString("end_time"));
            title_label.setText(text.getString("title"));
            type_label.setText(text.getString("type"));
            cancel_button.setText(text.getString("cancel"));
            save_button.setText(text.getString("save"));
            save_button.setOnMouseClicked(e -> createAppointment());
        }
    }

    private void getAppointments() {
        allAppointments.clear();
        String query = "select * from appointments";
        try (Statement stmt = Helper.con.createStatement()) {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                Appointment appointment;
                appointment = new Appointment(
                    rs.getInt("Appointment_ID"),
                    rs.getInt("Contact_ID"),
                    rs.getTimestamp("Create_Date").toLocalDateTime(),
                    rs.getString("Created_By"),
                    rs.getTimestamp("Last_Update").toLocalDateTime(),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getString("Description"),
                    rs.getTimestamp("End").toLocalDateTime(),
                    rs.getTimestamp("Start").toLocalDateTime(),
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
        selectedAppointment = null;
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
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime end = LocalDateTime.parse(edit_end.getText(), formatter);
            LocalDateTime  start = LocalDateTime.parse(edit_start.getText(), formatter);
            String location = edit_location.getText();
            Contact contact = edit_contact.getValue();
            Customer customer = edit_customer.getValue();
            User user = edit_user.getValue();
            String title = edit_title.getText();
            String type = edit_type.getText();
            String description = edit_description.getText();

            Date current_date = new Date(System.currentTimeMillis());
            String current_user = UserController.user.getUserName();
            String query = String.format("INSERT into appointments " +
                    "(Contact_ID, Create_Date, Created_By, Customer_ID, Description, End, Last_Update, Last_Updated_By, Location, Start, Title, Type, User_ID)" +
                    " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s);",
                    contact.getId(), current_date, current_user, customer.getId(), description, end, current_date, current_user, location, start, title, type, user.getId());
            try (Statement stmt = Helper.con.createStatement()) {
                if (stmt.executeUpdate(query) == 1) {
                    getAppointments();
                    close();
                } else {
                    throwAlert("Something went wrong", "Something went wrong");
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } catch (DateTimeException e) {
            throwAlert("Bad date entered", "Date must match format yyyy-MM-dd HH:mm:ss");
        }
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
            stage = (Stage) edit_id.getScene().getWindow();
        }
        stage.close();
    }
}




