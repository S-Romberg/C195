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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static Controllers.Helper.throwAlert;

public class AppointmentController {
    @FXML private RadioButton all;
    @FXML private RadioButton monthly;
    @FXML private RadioButton weekly;
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
            edit_start.setText(String.valueOf(selectedAppointment.getStartTime().format(Helper.formatter)));
            edit_end.setText(String.valueOf(selectedAppointment.getEndTime().format(Helper.formatter)));
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

    static void getAppointments() {
        allAppointments.clear();
        for(Customer customer : CustomerController.allCustomers) {
            allAppointments.addAll(customer.getAppointments());
        }
    }

    static void refreshAppointmentData() {
        for(Customer customer : CustomerController.allCustomers) {
            customer.getCustomerAppointments();
        }
    }

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

    public void deleteAppointment(MouseEvent mouseEvent) {
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

    private boolean invalidTime(LocalDateTime start, LocalDateTime end, Customer customer) {
        System.out.println("Invalid Time");
        return outsideBusinessHours(end) || outsideBusinessHours(start) || overlappingWithOtherAppointments(end, start, customer);
    }

    /**
     * @param time a time that should be within business hours in EST
     */
    private boolean outsideBusinessHours(LocalDateTime time) {
        LocalTime estTime = Appointment.toUTC(time).withZoneSameInstant(Helper.estTime).toLocalTime();
        return estTime.isBefore(eightAm) || estTime.isAfter(tenPm);
    }

    /**
     *  @param start new appointment start time, end new appointment end time, customer is customer that is associated with the appointment
     *  appointment overlaps if the end time or start time falls inside of a previously existing appointment
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
     */
    public void allAppointments(){
        filteredAppointments.setPredicate(s -> true);
    }

    /**
     *  set filter on appointments table to monthly appointments
     */
    public void monthlyAppointments(){
        System.out.println("CURRENT: " + currentDate);
        Predicate<Appointment> filterAppointments = e -> e.getStartTime().isAfter(currentDate) && e.getStartTime().isBefore(currentDate.plus(1, ChronoUnit.MONTHS));
        filteredAppointments.setPredicate(filterAppointments);
    }

    /**
     *  set filter on appointments table to weekly appointments
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
        if (appointment == null) { throwAlert("Error: No selected appointment", "Must select appointment to modify", ""); return; }
        Parent addAppointmentPage = FXMLLoader.load(getClass().getResource("../Views/appointment_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(addAppointmentPage));
        stage.show();
    }

}




