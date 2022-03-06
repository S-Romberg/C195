package Models;

import Controllers.ContactController;
import Controllers.CustomerController;
import Controllers.Helper;
import Controllers.UserController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

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

    public Appointment(int id, int contact_id, LocalDateTime create_date, String created_by, LocalDateTime updated_date, String updated_by, int customer_id, String description, LocalDateTime end_time, LocalDateTime start_time, String location, String title, String type, int user_id) throws ParseException {
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
     * @return the create_date in UTC
     */
    public LocalDateTime getCreateDateUtc() {
        return create_date;
    }

    /**
     * @return the updated_date in local time
     */
    public LocalDateTime getCreateDate() {
        return toUTC(create_date).withZoneSameInstant(Helper.localTime).toLocalDateTime();
    }


    /**
     * @return the created_by
     */
    public String getCreatedBy() {
        return created_by;
    }

    /**
     * @return the updated_date in UTC
     */
    public LocalDateTime getUpdatedDateUtc() {
        return updated_date;
    }

    /**
     * @return the updated_date in local time
     */
    public LocalDateTime getUpdatedDate() {
        return toUTC(updated_date).withZoneSameInstant(Helper.localTime).toLocalDateTime();
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
    public int getContactId() {
        return contact_id;
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
     * @return the start_time in local time
     */
    public LocalDateTime getStartTime() {
        return toUTC(start_time).withZoneSameInstant(Helper.localTime).toLocalDateTime();
    }

    /**
     * @return the start_time in utc
     */
    public LocalDateTime getStartTimeUtc() {
        return start_time;
    }

    /**
     * @return the start_time in est time
     */
    public LocalDateTime getStartTimeEst() {
        return toUTC(start_time).withZoneSameInstant(Helper.estTime).toLocalDateTime();
    }

    /**
     * @return the end_time in local time
     */
    public LocalDateTime getEndTime() {
        return toUTC(end_time).withZoneSameInstant(Helper.localTime).toLocalDateTime();
    }

    /**
     * @return the end_time in UTC
     */
    public LocalDateTime getEndTimeUtc() {
        return end_time;
    }

    /**
     * @return the end_time in est time
     */
    public LocalDateTime getEndTimeEst() {
        return toUTC(end_time).withZoneSameInstant(Helper.estTime).toLocalDateTime();
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

    public static ZonedDateTime toUTC(LocalDateTime time) {
        return ZonedDateTime.of(time, ZoneOffset.UTC );
    }
}
