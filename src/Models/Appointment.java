package Models;

import java.time.LocalDate;

public class Appointment {
    int id;
    Contact contact;
    LocalDate create_date;
    String created_by;
    LocalDate updated_date;
    String updated_by;
    Customer customer;
    String description;
    LocalDate end_time;
    LocalDate start_time;
    String location;
    String title;
    String type;
    int user_id;

    public Appointment(int id,
//                       Contact contact,
                       LocalDate create_date, String created_by, LocalDate updated_date, String updated_by, Customer customer, String description, LocalDate end_time, LocalDate start_time, String location, String title, String type, int user_id) {
        this.id = id;
//        this.contact = contact;
        this.create_date = create_date;
        this.created_by = created_by;
        this.updated_date = updated_date;
        this.updated_by = updated_by;
        this.customer = customer;
        this.description = description;
        this.end_time = end_time;
        this.start_time = start_time;
        this.location = location;
        this.title = title;
        this.type = type;
        this.user_id = user_id;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

}
