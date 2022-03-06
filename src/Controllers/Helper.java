package Controllers;
import javafx.scene.control.Alert;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

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
