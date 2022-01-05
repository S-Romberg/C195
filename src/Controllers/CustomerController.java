package Controllers;

import Models.Customer;
import Models.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.event.ChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CustomerController {
    @FXML
    private TableView<Customer> customer_table;
    @FXML
    private TableColumn<Customer, Integer> id;
    @FXML
    private TableColumn<Customer, String> address;
    @FXML
    private TableColumn<Customer, String> name;
    @FXML
    private TableColumn<Customer, String> country;
    @FXML
    private TableColumn<Customer, String> division;
    @FXML
    private TableColumn<Customer, String> phone;
    @FXML
    private TableColumn<Customer, String> postal_code;
    @FXML
    private TableColumn<Customer, LocalDate> create_date;
    @FXML
    private TableColumn<Customer, String> created_by;
    @FXML
    private TableColumn<Customer, LocalDate> last_update;
    @FXML
    private TableColumn<Customer, String> updated_by;
    @FXML
    private TextField edit_address;
    @FXML
    private TextField edit_name;
    @FXML
    private ChoiceBox<String> edit_country;
    @FXML
    private ChoiceBox<String> edit_division;
    @FXML
    private TextField edit_postal_code;
    @FXML
    private TextField edit_phone;
    @FXML
    private Button save_button;

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    ArrayList<Division> divisions = new ArrayList<>();
    public static ObservableList<String> allDivisions = FXCollections.observableArrayList();
    public static ObservableList<String> allCountries = FXCollections.observableArrayList();

    private static Customer selectedCustomer;
    private static int country_id;
    ResourceBundle text;
    ResultSet rs;

    public void initialize() {
        Controller.connectToAndQueryDatabase();
        setLocalDefault();
        if (customer_table != null) {
            getCustomers();
            customer_table.setItems(allCustomers);
        } else {
            getCountries();
            getAndSetDivisions("");
            setFormValues();
            edit_country.setOnAction(event -> getAndSetDivisions(edit_country.getValue()));
        }
    }

    private void setFormValues() {
        edit_country.setItems(allCountries);
        edit_division.setItems(allDivisions);

        if (selectedCustomer != null) {
            edit_address.setText(selectedCustomer.getAddress());
            edit_name.setText(selectedCustomer.getName());
//          edit_country.
//          edit_division.
            edit_phone.setText(selectedCustomer.getPhone());
            edit_postal_code.setText(selectedCustomer.getPostalCode());
            save_button.setOnMouseClicked(e -> Customer.updateCustomer());
        }
    }

    private void setLocalDefault() {
        Locale currentLocale = Locale.getDefault();
        text = ResourceBundle.getBundle("TextBundle", currentLocale);
        if(customer_table != null) {
            id.setText(text.getString("id"));
            address.setText(text.getString("address"));
            name.setText(text.getString("name"));
            country.setText(text.getString("country"));
            division.setText(text.getString("division"));
            phone.setText(text.getString("phone"));
            postal_code.setText(text.getString("postal_code"));
            create_date.setText(text.getString("create_date"));
            created_by.setText(text.getString("created_by"));
            last_update.setText(text.getString("last_update"));
            updated_by.setText(text.getString("last_updated_by"));
        }
    }

    private void getCustomers() {
        String query = "select * from customers " +
                "join first_level_divisions fld on customers.Division_ID = fld.Division_ID " +
                "join countries c on fld.Country_ID = c.Country_ID;";
        try (Statement stmt = Controller.con.createStatement()) {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getDate("Create_Date").toLocalDate(),
                        rs.getString("Created_By"),
                        rs.getString("Division"),
                        rs.getString("Country"),
                        rs.getDate("Last_Update").toLocalDate(),
                        rs.getString("Last_Updated_By"),
                        rs.getString("Phone"),
                        rs.getString("Postal_Code")
                );
                addCustomer(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void getCountries() {
        String query = "select * from countries";
        try (Statement stmt = Controller.con.createStatement()) {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                addCountry(rs.getString("Country"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void getAndSetDivisions(String country) {
        if (divisions.isEmpty()) {
            String query;
            query = "select * from first_level_divisions fld join countries c on fld.Country_ID = c.Country_ID";
            try (Statement stmt = Controller.con.createStatement()) {
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Division division = new Division(rs.getString("Country"), rs.getString("Division"));
                    divisions.add(division);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else  {
            allDivisions.clear();
            divisions.forEach(division -> updatedFilteredDivisions(division, country));
        }
    }


    public void updatedFilteredDivisions(Division division, String country) {
        if(division.getCountry().equals(country)) {
            allDivisions.add(division.getDivision());
        }
    }

    /**
     * creates add customer scene
     */
    public void addCustomer() throws Exception {
        setSelectedCustomer(null);
        Parent addCustomerPage = FXMLLoader.load(getClass().getResource("../Views/customer_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(addCustomerPage));
        stage.show();
    }

    /**
     * creates modify customer scene, sets selectedCustomer to customer selected on table
     */
    public void modifyCustomer() throws Exception {
        Customer customer = customer_table.getSelectionModel().getSelectedItem();
        setSelectedCustomer(customer);
        if (customer == null) { Controller.throwAlert("Error: No selected customer", "Must select customer to modify"); return; }
        Parent addCustomerPage = FXMLLoader.load(getClass().getResource("../Views/customer_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(addCustomerPage));
        stage.show();
    }

    /**
     * @param customer the selected customer in customer table
     */
    public static void setSelectedCustomer(Customer customer){
        selectedCustomer = customer;
    }

    private static void addCustomer(Customer newCustomer){
        allCustomers.add(newCustomer);
    }

    private static void addCountry(String newCountry){
        allCountries.add(newCountry);
    }

}
