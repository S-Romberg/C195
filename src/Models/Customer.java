package Models;

import java.sql.Date;

public class Customer {
    public int id;
    public String name;
    public String address;
    public Date create_date;
    public int created_by;
    public int division_id;
    public Date update_date;
    public int updated_by;
    public String phone;
    public String postal_code;
//    public Division division;
//    public Country country;
//    public Appointments[] appointments;

    public Customer(int id, String name, String address, Date create_date, int created_by, int division_id, Date update_date, int updated_by, String phone, String postal_code) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.create_date = create_date;
        this.created_by = created_by;
        this.division_id = division_id;
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
}