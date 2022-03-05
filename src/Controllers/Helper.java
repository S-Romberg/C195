package Controllers;
import javafx.scene.control.Alert;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

//2.  Write code that provides the following customer record functionalities:

//-  When deleting a customer record, all of the customer’s appointments must be deleted first, due to foreign key constraints.
//
//•  When a customer record is deleted, a custom message should display in the user interface.
//
//•  A custom message is displayed in the user interface with the Appointment_ID and type of appointment canceled.
//
//c.  Write code that enables the user to adjust appointment times. While the appointment times should be stored in Coordinated Universal Time (UTC), they should be automatically and consistently updated according to the local time zone set on the user’s computer wherever appointments are displayed in the application.
//
//
//d.  Write code to implement input validation and logical error checks to prevent each of the following changes when adding or updating information; display a custom message specific for each error check in the user interface:
//
//•  scheduling an appointment outside of business hours defined as 8:00 a.m. to 10:00 p.m. EST, including weekends
//
//•  scheduling overlapping appointments for customers
//
//•  entering an incorrect username and password
//
//
//e.  Write code to provide an alert when there is an appointment within 15 minutes of the user’s log-in. A custom message should be displayed in the user interface and include the appointment ID, date, and time. If the user does not have any appointments within 15 minutes of logging in, display a custom message in the user interface indicating there are no upcoming appointments.
//
//
//Note: Since evaluation may be testing your application outside of business hours, your alerts must be robust enough to trigger an appointment within 15 minutes of the local time set on the user’s computer, which may or may not be EST.
//
//
//f.  Write code that generates accurate information in each of the following reports and will display the reports in the user interface:
//
//
//Note: You do not need to save and print the reports to a file or provide a screenshot.
//
//•  the total number of customer appointments by type and month
//
//•  a schedule for each contact in your organization that includes appointment ID, title, type and description, start date and time, end date and time, and customer ID
//
//•  an additional report of your choice that is different from the two other required reports in this prompt and from the user log-in date and time stamp that will be tracked in part C
//
//C.  Write code that provides the ability to track user activity by recording all user log-in attempts, dates, and time stamps and whether each attempt was successful in a file named login_activity.txt. Append each new record to the existing file, and save to the root folder of the application.
//
//
//D.  Provide descriptive Javadoc comments for at least 70 percent of the classes and their members throughout the code, and create an index.html file of your comments to include with your submission based on Oracle’s guidelines for the Javadoc tool best practices. Your comments should include a justification for each lambda expression in the method where it is used.
//
//Note: The comments on the lambda need to be located in the comments describing the method where it is located for it to export properly.
//
//
//E.  Create a README.txt file that includes the following information:
//
//•  title and purpose of the application
//
//•  author, contact information, student application version, and date
//
//•  IDE including version number (e.g., IntelliJ Community 2020.01), full JDK of version 11 used (e.g., Java SE 11.0.4), and JavaFX version compatible with JDK 11 (e.g. JavaFX-SDK-11.0.2)
//
//•  directions for how to run the program
//
//•  a description of the additional report of your choice you ran in part A3f
//
//•  the MySQL Connector driver version number, including the update number (e.g., mysql-connector-java-8.1.23)

public class Helper {
    public static Connection con;
    public static ZoneId localTime;
    public static ZoneId estTime;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void connectToAndQueryDatabase() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/client_schedule",
                    "spencerromberg", "");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void throwAlert(String mainText, String detail) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(mainText);
        alert.setHeaderText("Error");
        alert.setContentText(detail);
        alert.showAndWait();
    }


    public Helper() {
    }

    /**
     * Initializes parsers for different time zones.
     * Then it sets the system time zone to UTC so the SQL
     * datetime won't be changed when it's parsed
     */
    public static void initializeTimeZones() {
        localTime = ZoneId.systemDefault();
        estTime = ZoneId.of("America/New_York");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static LocalDateTime localTimeToUtc(LocalDateTime time) {
        return ZonedDateTime.of(time, localTime).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

}
