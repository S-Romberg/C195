package Controllers;

import Models.Appointment;
import Models.Contact;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class SchedulesController {
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
    @FXML private TableView<Appointment> appointments;
    @FXML private ChoiceBox<Contact> contacts;

    Locale currentLocale = Locale.getDefault();
    ResourceBundle text =  ResourceBundle.getBundle("TextBundle", currentLocale);
    public static FilteredList<Appointment> appointmentsByContacts = new FilteredList<>(AppointmentController.allAppointments);

    public void initialize() {
        setLocalDefault();
        ContactController.getAllContacts();
        contacts.setItems(ContactController.allContacts);
        AppointmentController.getAppointments();
        appointments.setItems(appointmentsByContacts);
        contacts.getSelectionModel().selectedIndexProperty().addListener(obs-> {
            Predicate<Appointment> filterAppointments = a -> contacts.getValue().equals(a.getContact());
            if (contacts.getValue() != null) {
                appointmentsByContacts.setPredicate(filterAppointments);
            } else {
                appointmentsByContacts.setPredicate(s -> true);
            }
        });
    }

    private void setLocalDefault() {
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
    }
}
