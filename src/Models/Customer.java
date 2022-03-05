package Models;

import Controllers.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer {
    public int id;
    public String name;
    public String address;
    public LocalDateTime create_date;
    public String created_by;
    public String division;
    public String country;
    public LocalDateTime update_date;
    public String updated_by;
    public String phone;
    public String postal_code;
    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public Customer(int id, String name, String address, LocalDateTime create_date, String created_by, String division, String country, LocalDateTime update_date, String updated_by, String phone, String postal_code) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.create_date = create_date;
        this.created_by = created_by;
        this.division = division;
        this.country = country;
        this.update_date = update_date;
        this.updated_by = updated_by;
        this.phone = phone;
        this.postal_code = postal_code;
        this.appointments = FXCollections.observableArrayList();
        getCustomerAppointments();
    }

    public Customer() {

    }

    /**
     *  queries the DB for all appointments with the customers Customer_ID
     */
    public void getCustomerAppointments() {
        this.appointments.clear();
        String query = String.format("select * from appointments where Customer_ID = %s", id);
        try (Statement stmt = Helper.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
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
                addAppointment(appointment);
            }
        } catch (SQLException | ParseException throwable) {
            throwable.printStackTrace();
        }
    }

    public ObservableList<Appointment> getAppointments() {
        return this.appointments;
    }

    /**
     * @param appointment is an Appointment belonging to a customer
     */
    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id sets id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name sets name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return updated by
     */
    public String getUpdatedBy() {
        return updated_by;
    }

    /**
     * @param updated_by sets updated_by
     */
    public void setUpdatedBy(String updated_by) {
        this.updated_by = updated_by;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address sets address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country sets country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division sets division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone sets phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return postal code
     */
    public String getPostalCode() {
        return postal_code;
    }

    /**
     * @param postal_code sets postal_code By
     */
    public void setPostalCode(String postal_code) {
        this.postal_code = postal_code;
    }

    /**
     * @return Create Date
     */
    public LocalDateTime getCreateDate() {
        return create_date;
    }

    /**
     * @param create_date sets created By
     */
    public void setCreateDate(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    /**
     * @return Update Date
     */
    public LocalDateTime getUpdateDate() {
        return update_date;
    }

    /**
     * @param update_date sets created By
     */
    public void setUpdateDate(LocalDateTime update_date) {
        this.update_date = update_date;
    }

    /**
     * @return Created By
     */
    public String getCreatedBy() {
        return created_by;
    }
    /**
     * @param created_by sets created By
     */
    public void setCreatedBy(String created_by) {
         this.created_by = created_by;
    }

    @Override
    public String toString() {
        return getName();
    }
}