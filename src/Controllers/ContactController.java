package Controllers;

import Models.Contact;
import Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactController {
    public static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    public static Contact contact;

    public static void getAllContacts() {
        allContacts.clear();
        String query = "SELECT * FROM contacts;";
        try (Statement stmt = Helper.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                contact = new Contact(
                    rs.getInt("Contact_ID"),
                    rs.getString("Contact_Name"),
                    rs.getString("Email"));
                allContacts.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Contact findContact(int id) {
        Contact match = null;

        for (Contact c : allContacts) {
            if (c.getId() == id) {
                match = c;
            }
        };
        return match;
    }

}