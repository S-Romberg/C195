package Models;

import java.sql.Date;
import java.time.LocalDate;

public class Customer {
    public int id;
    public String name;
    public String address;
    public LocalDate create_date;
    public String created_by;
    public String division;
    public String country;
    public LocalDate update_date;
    public String updated_by;
    public String phone;
    public String postal_code;
//    public Appointments[] appointments;

    public Customer(int id, String name, String address, LocalDate create_date, String created_by, String division, String country, LocalDate update_date, String updated_by, String phone, String postal_code) {
        System.out.println(name + address + created_by);
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
    }

    public Customer() {

    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
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
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return postal code
     */
    public String getPostalCode() {
        return postal_code;
    }

    /**
     * @return Create Date
     */
    public LocalDate getCreateDate() {
        return create_date;
    }

    /**
     * @return Create Date
     */
    public LocalDate getUpdateDate() {
        return update_date;
    }

    /**
     * @return Created By
     */
    public String getCreatedBy() {
        return created_by;
    }
}