package Controllers;

import Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

public class CustomerController {
    @FXML
    private TableView<Customer> customer_table;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn address;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn country;
    @FXML
    private TableColumn division;
    @FXML
    private TableColumn phone;
    @FXML
    private TableColumn postal_code;
    @FXML
    private TableColumn create_date;
    @FXML
    private TableColumn created_by;
    @FXML
    private TableColumn last_update;
    @FXML
    private TableColumn updated_by;

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    ResourceBundle text;
    ResultSet rs;

    public void initialize() {
        Controller.connectToAndQueryDatabase();
        setLocalDefault();
        getCustomers();
        customer_table.setItems(allCustomers);
    }

    public static void addCustomer(Customer newCustomer){
        allCustomers.add(newCustomer);
    }

    private void setLocalDefault() {
        Locale currentLocale = Locale.getDefault();
        text = ResourceBundle.getBundle("TextBundle", currentLocale);
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
        } finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { Controller.con.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

}
