package Controllers;

import Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactController {
    public static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    public static Contact contact;

    public static void getAllContacts() {
        String query = "SELECT * FROM contacts;";
        try (Statement stmt = Helper.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
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

}