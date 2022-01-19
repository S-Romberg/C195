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
     * @return the create_date
     */
    public LocalDate getCreateDate() {
        return create_date;
    }

    /**
     * @return the created_by
     */
    public String getCreatedBy() {
        return created_by;
    }

    /**
     * @return the updated_date
     */
    public LocalDate getUpdatedDate() {
        return updated_date;
    }

    /**
     * @return the updated_by
     */
    public String getUpdatedBy() {
        return updated_by;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @return the customer
     */
    public String getCustomerName() {
        return customer.getName();
    }

    /**
     * @return the start_time
     */
    public LocalDate getStartTime() {
        return start_time;
    }

    /**
     * @return the end_time
     */
    public LocalDate getEndTime() {
        return end_time;
    }

     /**
     * @return the location
     */
    public String getLocationString() {
        return location;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the user_id
     */
    public int getUserId() {
        return user_id;
    }

}
