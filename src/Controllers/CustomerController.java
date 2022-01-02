package Controllers;

import Models.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class CustomerController {
    @FXML
    private TableView<Customer> customer_table;

    public Customer getCustomers() {
        return new Customer();
    }
}
