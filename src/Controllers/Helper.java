package Controllers;
import javafx.scene.control.Alert;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


//D.  Provide descriptive Javadoc comments for at least 70 percent of the classes and their members throughout the code, and create an index.html file of your comments to include with your submission based on Oracle’s guidelines for the Javadoc tool best practices. Your comments should include a justification for each lambda expression in the method where it is used.
//
//Note: The comments on the lambda need to be located in the comments describing the method where it is located for it to export properly.
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
    public static void throwAlert(String mainText, String detail, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(mainText);
        alert.setHeaderText(header);
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
