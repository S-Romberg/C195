package Models;

import Controllers.ContactController;
import Controllers.CustomerController;
import Controllers.UserController;

import java.time.LocalDateTime;

public class Appointment {
    int id;
    int contact_id;
    LocalDateTime create_date;
    String created_by;
    LocalDateTime updated_date;
    String updated_by;
    int customer_id;
    String description;
    LocalDateTime end_time;
    LocalDateTime start_time;
    String location;
    String title;
    String type;
    int user_id;

    public Appointment(int id, int contact_id, LocalDateTime create_date, String created_by, LocalDateTime updated_date, String updated_by, int customer_id, String description, LocalDateTime end_time, LocalDateTime start_time, String location, String title, String type, int user_id) {
        this.id = id;
        this.contact_id = contact_id;
        this.create_date = create_date;
        this.created_by = created_by;
        this.updated_date = updated_date;
        this.updated_by = updated_by;
        this.customer_id = customer_id;
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
    public LocalDateTime getCreateDate() {
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
    public LocalDateTime getUpdatedDate() {
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
        return CustomerController.findCustomer(customer_id);

    }

    /**
     * @return the customer name
     */
    public String getCustomerName() {
        return getCustomer().getName();
    }

    /**
     * @return the contact
     */
    public Contact getContact() {
        return ContactController.findContact(contact_id);
    }

    /**
     * @return the contact name
     */
    public String getContactName() {
        return getContact().getName();
    }

    /**
     * @return the start_time
     */
    public LocalDateTime getStartTime() {
        return start_time;
    }

    /**
     * @return the end_time
     */
    public LocalDateTime getEndTime() {
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

    /**
     * @return the user
     */
    public User getUser() {
        return UserController.getUser(user_id);
    }

}
