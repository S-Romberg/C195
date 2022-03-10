package Controllers;

import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static Controllers.Helper.throwAlert;

/**
 * Controls and populates the appointments view, the edit appointments view, and the add appointments view
 */
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
    public static FilteredList<Appointment> filteredAppointments = new FilteredList<>(allAppointments);

    LocalDateTime currentDate = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).withZoneSameInstant(Helper.localTime).toLocalDateTime();
    Locale currentLocale = Locale.getDefault();
    ResourceBundle text =  ResourceBundle.getBundle("TextBundle", currentLocale);
    LocalTime eightAm = LocalTime.of(8, 0);
    LocalTime tenPm = LocalTime.of(22, 0);
    static Appointment selectedAppointment;
    static ResultSet rs;


    public void initialize() {
        setLocalDefault();
        Helper.connectToAndQueryDatabase();
        if (appointment_table != null) {
            getAppointments();
            appointment_table.setItems(filteredAppointments);
        } else if (edit_user != null){
            setFormValues();
        }
    }

    /**
     * Sets initial form values when editing an appointment
     * Sets choicebox items for customers, contacts, and users
     */
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
            edit_start.setText(selectedAppointment.getStartTime().format(Helper.formatter));
            edit_end.setText(selectedAppointment.getEndTime().format(Helper.formatter));
            edit_title.setText(selectedAppointment.getTitle());
            edit_type.setText(selectedAppointment.getType());
            edit_description.setText(selectedAppointment.getDescription());
            edit_contact.setValue(selectedAppointment.getContact());
            edit_customer.setValue(selectedAppointment.getCustomer());
            edit_user.setValue(selectedAppointment.getUser());
            save_button.setOnMouseClicked(e -> updateAppointment());
        }
    }

    /**
     * Sets all text in view to users local default language
     */
    private void setLocalDefault() {
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

    /**
     * Iterates through customers to grab all Appointment instances
     * Stores Appointment instances in allAppointments observable array
     */
    static void getAppointments() {
        allAppointments.clear();
        for(Customer customer : CustomerController.allCustomers) {
            allAppointments.addAll(customer.getAppointments());
        }
    }

    /**
     * Iterates through customers, calls getCustomerAppointments on each customer
     * This refreshes all customer data by re-querying the database
     */
    static void refreshAppointmentData() {
        for(Customer customer : CustomerController.allCustomers) {
            customer.getCustomerAppointments();
        }
    }

    /**
     * Grabs all values form the appointment form
     * if values are valid, creates a new Appointment record in the database
     * then refreshes appointment data and refreshes allAppointments array
     *
     * Throws alert if something goes wrong, or if the record is invalid
     */
    public void createAppointment() {
        try {
            LocalDateTime end = Helper.localTimeToUtc(LocalDateTime.parse(edit_end.getText(), Helper.formatter));
            LocalDateTime start = Helper.localTimeToUtc(LocalDateTime.parse(edit_start.getText(), Helper.formatter));
            if(invalidTime(end, start, edit_customer.getValue())) {
                throwAlert(text.getString("outside_business_hours"), text.getString("outside_business_hours"), "");
                return;
            }
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
                    refreshAppointmentData();
                    getAppointments();
                    close();
                } else {
                    throwAlert(text.getString("generic_error"), text.getString("generic_error"), "");
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } catch (DateTimeException e) {
            throwAlert("Bad date entered", "Date must match format yyyy-MM-dd HH:mm", "");
        }
    }

    /**
     * Grabs all values form the appointment form
     * if values are valid, updates the Appointment record in the database
     * with matching ID from form. Then refreshes appointment data and refreshes allAppointments array
     *
     * Throws alert if something goes wrong, or if the record is invalid
     */
    public void updateAppointment() {
        try {
            int id = Integer.parseInt(edit_id.getText());
            LocalDateTime end = Helper.localTimeToUtc(LocalDateTime.parse(edit_end.getText(), Helper.formatter));
            LocalDateTime start = Helper.localTimeToUtc(LocalDateTime.parse(edit_start.getText(), Helper.formatter));
            if(invalidTime(end, start, edit_customer.getValue())) {
                throwAlert(text.getString("outside_business_hours"), text.getString("outside_business_hours"), "");
                return;
            }
            String location = edit_location.getText();
            Contact contact = edit_contact.getValue();
            Customer customer = edit_customer.getValue();
            User user = edit_user.getValue();
            String title = edit_title.getText();
            String type = edit_type.getText();
            String description = edit_description.getText();
            Date current_date = new Date(System.currentTimeMillis());
            String current_user = UserController.user.getUserName();
            String query = String.format("UPDATE appointments SET Contact_ID = '%s', Customer_ID = '%s', Description = '%s', End = '%s'," +
                            "Last_Update = '%s', Last_Updated_By = '%s', Location = '%s', Start = '%s', Title = '%s', Type = '%s', User_ID = '%s' WHERE Appointment_ID = '%s';",
                    contact.getId(), customer.getId(), description, end, current_date, current_user, location, start, title, type, user.getId(), id);
            try (Statement stmt = Helper.con.createStatement()) {
                if (stmt.executeUpdate(query) == 1) {
                    refreshAppointmentData();
                    getAppointments();
                    close();
                } else {
                    throwAlert(text.getString("generic_error"), text.getString("generic_error"), "");
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } catch (DateTimeException e) {
            throwAlert("Bad date entered", "Date must match format yyyy-MM-dd HH:mm:ss", "");
        }
    }

    /**
     * Grabs selected appointment from appointment_table, queries the database to delete
     * the record.
     *
     * Throws alert if something goes wrong
     * Throws alert when record is deleted, informing the user that the appointment has been deleted
     */
    public void deleteAppointment() {
        Appointment appointment = appointment_table.getSelectionModel().getSelectedItem();
        setSelectedAppointment(appointment);
        if (customer == null) { throwAlert("Error: No selected appointment", "Must select appointment to delete", ""); return; }
        int id = selectedAppointment.getId();
        String query = String.format("DELETE from appointments WHERE Appointment_ID = '%s';", id);
        try (Statement stmt = Helper.con.createStatement()) {
            if (stmt.executeUpdate(query) == 1) {
                throwAlert(text.getString("deleted_appointment"), text.getString("deleted_appointment") + " #" + appointment.getId() + " - " + appointment.getType(), "");
                refreshAppointmentData();
                getAppointments();
            } else {
                throwAlert(text.getString("generic_error"), text.getString("generic_error"), "");

            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Checks if times for an appointment are invalid or valid
     * @return boolean true if the times are invalid, false if the times are valid
     * @param start a LocalDateTime that the user entered as a start time for appointment
     * @param end a LocalDateTime that the user entered as an end time for appointment
     * @param customer the customer associated with the appointment that the user is trying to edit or create
     */
    private boolean invalidTime(LocalDateTime start, LocalDateTime end, Customer customer) {
        System.out.println("Invalid Time");
        return outsideBusinessHours(end) || outsideBusinessHours(start) || overlappingWithOtherAppointments(end, start, customer);
    }

    /**
     * @return boolean true if the time entered is outside business hours in EST, false if time entered is inside business hours in EST
     * @param time a time that should be within business hours in EST
     */
    private boolean outsideBusinessHours(LocalDateTime time) {
        LocalTime estTime = Appointment.toUTC(time).withZoneSameInstant(Helper.estTime).toLocalTime();
        return estTime.isBefore(eightAm) || estTime.isAfter(tenPm);
    }

    /**
     * Appointment overlaps if the end time or start time falls inside of a previously existing appointment
     *
     * @return boolean true if appointment times overlap with any other appointments that the customer has, false if the appointment does not overlap with other appointments on the customers calendar
     *  @param start new appointment start time
     *  @param end new appointment end time
     *  @param customer is customer that is associated with the appointment
     */
    private boolean overlappingWithOtherAppointments(LocalDateTime start, LocalDateTime end, Customer customer) {
        boolean overlapping = false;
        end = Appointment.toUTC(end).withZoneSameInstant(Helper.localTime).toLocalDateTime();
        start = Appointment.toUTC(start).withZoneSameInstant(Helper.localTime).toLocalDateTime();
        for (Appointment appointment : customer.getAppointments()) {
            if (end.isBefore(appointment.getEndTime()) && end.isAfter(appointment.getStartTime())) {
                overlapping = true;
            }
            if (start.isBefore(appointment.getEndTime()) && start.isAfter(appointment.getStartTime())) {
                overlapping = true;
            }
            if (end.isEqual(appointment.getEndTime()) || end.isEqual(appointment.getStartTime()) || start.isEqual(appointment.getStartTime()) || start.isEqual(appointment.getEndTime())) {
                overlapping = true;
            }
        }
        return overlapping;
    }

    /**
     * @param appointment the selected appointment in appointment table
     */
    public static void setSelectedAppointment(Appointment appointment){
        selectedAppointment = appointment;
    }

    /**
     *  set filter on appointments table to all appointments
     *  Includes predicate with lambda to return true for all appointments, so all appointments are shown
     *  A lambda was used here because Predicates are one of the perfect use cases for lambdas. They take in values and immediately return a value in a simple block of code
     */
    public void allAppointments(){
        filteredAppointments.setPredicate(s -> true);
    }

    /**
     *  set filter on appointments table to monthly appointments
     *  Includes predicate with lambda to return true if the start time for an appointment is after the current moment and within one month
     *  A lambda was used here because Predicates are one of the perfect use cases for lambdas. They take in values and immediately return a value in a simple block of code
     */
    public void monthlyAppointments(){
        Predicate<Appointment> filterAppointments = e -> e.getStartTime().isAfter(currentDate) && e.getStartTime().isBefore(currentDate.plus(1, ChronoUnit.MONTHS));
        filteredAppointments.setPredicate(filterAppointments);
    }

    /**
     *  set filter on appointments table to weekly appointments
     *  Includes predicate with lambda to return true if the start time for an appointment is after the current moment and within one week
     *  A lambda was used here because Predicates are one of the perfect use cases for lambdas. They take in values and immediately return a value in a simple block of code
     */
    public void weeklyAppointments(){
        Predicate<Appointment> filterAppointments = e -> e.getStartTime().isAfter(currentDate) && e.getStartTime().isBefore(currentDate.plus(1, ChronoUnit.WEEKS));
        filteredAppointments.setPredicate(filterAppointments);
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

    /**
     * opens add appointment form scene
     * @throws IOException if something goes wrong
     */
    public void addAppointment() throws IOException {
        selectedAppointment = null;
        Parent addAppointmentPage = FXMLLoader.load(getClass().getResource("../Views/appointment_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(addAppointmentPage));
        stage.show();
    }
    /**
     * opens edit appointment form scene
     * @throws IOException if something goes wrong
     */
    public void modifyAppointment() throws IOException {
        Appointment appointment = appointment_table.getSelectionModel().getSelectedItem();
        setSelectedAppointment(appointment);
        if (appointment == null) { throwAlert("Error: No selected appointment", "Must select appointment to modify", ""); return; }
        Parent addAppointmentPage = FXMLLoader.load(getClass().getResource("../Views/appointment_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(addAppointmentPage));
        stage.show();
    }
}




