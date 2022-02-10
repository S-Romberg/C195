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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static Controllers.Helper.throwAlert;

public class CustomerController {
    private static ResultSet rs;
    @FXML private TableView<Customer> customer_table;
    @FXML private TableColumn<Customer, Integer> id;
    @FXML private TableColumn<Customer, String> address;
    @FXML private TableColumn<Customer, String> name;
    @FXML private TableColumn<Customer, String> country;
    @FXML private TableColumn<Customer, String> division;
    @FXML private TableColumn<Customer, String> phone;
    @FXML private TableColumn<Customer, String> postal_code;
    @FXML private TableColumn<Customer, LocalDateTime> create_date;
    @FXML private TableColumn<Customer, String> created_by;
    @FXML private TableColumn<Customer, LocalDateTime> last_update;
    @FXML private TableColumn<Customer, String> updated_by;
    @FXML private TextField edit_id;
    @FXML private TextField edit_address;
    @FXML private TextField edit_name;
    @FXML private ChoiceBox<String> edit_country;
    @FXML private ChoiceBox<String> edit_division;
    @FXML private TextField edit_postal_code;
    @FXML private TextField edit_phone;
    @FXML private Button save_button;
    @FXML private Button delete_button;
    @FXML private Button add_button;
    @FXML private Button modify_button;

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    ArrayList<Division> divisions = new ArrayList<>();
    public static ObservableList<String> allDivisions = FXCollections.observableArrayList();
    public static ObservableList<String> allCountries = FXCollections.observableArrayList();

    private static Customer selectedCustomer;
    ResourceBundle text;

    public void initialize() {
        Helper.connectToAndQueryDatabase();
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
            edit_id.setText(String.valueOf(selectedCustomer.getId()));
            edit_address.setText(selectedCustomer.getAddress());
            edit_name.setText(selectedCustomer.getName());
            edit_country.setValue(selectedCustomer.getCountry());
            edit_division.setValue(selectedCustomer.getDivision());
            edit_phone.setText(selectedCustomer.getPhone());
            edit_postal_code.setText(selectedCustomer.getPostalCode());
            save_button.setOnMouseClicked(e -> updateCustomer());
        } else {
            save_button.setOnMouseClicked(e -> createCustomer());
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
            add_button.setText(text.getString("add"));
            modify_button.setText(text.getString("modify"));
            delete_button.setText(text.getString("delete"));
        }
    }

    public static void getCustomers() {
        allCustomers.clear();
        String query = "select * from customers " +
                "join first_level_divisions fld on customers.Division_ID = fld.Division_ID " +
                "join countries c on fld.Country_ID = c.Country_ID;";
        try (Statement stmt = Helper.con.createStatement()) {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getTimestamp("Create_Date").toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getString("Division"),
                        rs.getString("Country"),
                        rs.getTimestamp("Last_Update").toLocalDateTime(),
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
        try (Statement stmt = Helper.con.createStatement()) {
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
            try (Statement stmt = Helper.con.createStatement()) {
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Division division = new Division(rs.getInt("Division_ID"), rs.getString("Country"), rs.getString("Division"));
                    divisions.add(division);
                    allDivisions.add(division.getDivision());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else  {
            allDivisions.clear();
            divisions.forEach(division -> updatedFilteredDivisions(division, country));
        }
    }

    public Division findDivision(String name) {
        Division match = null;
        for (Division d : divisions) {
            if (d.getDivision().equals(name)) {
                match = d;
            }
        };
        return match;
    }

    public static Customer findCustomer(int id) {
        Customer match = null;
        for (Customer d : allCustomers) {
            if (d.getId() == id) {
                match = d;
            }
        };
        return match;
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
        if (customer == null) { throwAlert("Error: No selected customer", "Must select customer to modify"); return; }
        Parent addCustomerPage = FXMLLoader.load(getClass().getResource("../Views/customer_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(addCustomerPage));
        stage.show();
    }

    public void createCustomer() {
        String address = edit_address.getText();
        String name = edit_name.getText();
        Division division = findDivision(edit_division.getValue());
        String postal_code = edit_postal_code.getText();
        String phone = edit_phone.getText();
        Date current_date = new Date(System.currentTimeMillis());
        String current_user = UserController.user.getUserName();
        // String.format("u1=%s;u2=%s;u3=%s;u4=%s;", u1, u2, u3, u4);
        String query = String.format("INSERT into customers " +
                "(Address, Create_Date, Created_By, Customer_Name, Division_ID, Last_Update, Last_Updated_By, Phone, Postal_Code)" +
                " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", address, current_date, current_user, name, division.getId(), current_date, current_user, phone, postal_code);
        try (Statement stmt = Helper.con.createStatement()) {
            if (stmt.executeUpdate(query) == 1) {
                getCustomers();
                close();
            } else {
                throwAlert("Something went wrong", "Something went wrong");
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void updateCustomer() {
        int id = Integer.parseInt(edit_id.getText());
        String address = edit_address.getText();
        String name = edit_name.getText();
        Division division = findDivision(edit_division.getValue());
        String postal_code = edit_postal_code.getText();
        String phone = edit_phone.getText();
        String query = String.format(
                "UPDATE customers SET Address = '%s', Customer_Name = '%s', Division_ID = '%s', Phone = '%s', Postal_Code = '%s', Last_Update = '%s'," +
                        " Last_Updated_By = '%s' WHERE Customer_ID = '%s';",
                address, name, division.getId(), phone, postal_code, new Date(System.currentTimeMillis()), UserController.user.getUserName(), id);
        try (Statement stmt = Helper.con.createStatement()) {
            if (stmt.executeUpdate(query) == 1) {
                getCustomers();
                close();
            } else {
                throwAlert("Something went wrong", "Something went wrong");
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void deleteCustomer() {
        Customer customer = customer_table.getSelectionModel().getSelectedItem();
        setSelectedCustomer(customer);
        if (customer == null) { throwAlert("Error: No selected customer", "Must select customer to delete"); return; }
        int id = selectedCustomer.getId();
        String query = String.format("DELETE from customers WHERE Customer_ID = '%s';", id);
        try (Statement stmt = Helper.con.createStatement()) {
            if (stmt.executeUpdate(query) == 1) {
                getCustomers();
            } else {
                throwAlert("Something went wrong", "Something went wrong");
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
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

    /**
     * closes scene
     */
    public void close() {
        Stage stage;
        if (customer_table != null) {
            stage = (Stage) customer_table.getScene().getWindow();
        } else {
            stage = (Stage) edit_address.getScene().getWindow();
        }
        stage.close();
    }
}
