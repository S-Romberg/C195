package Controllers;

import Models.Appointment;
import Models.Report;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportsController {

    @FXML private Label type_report;
    @FXML private Label month_report;
    @FXML private Label user_report;
    @FXML private TableColumn<Report, String> type;
    @FXML private TableColumn<Report, String>  type_count;
    @FXML private TableColumn<Report, String> month;
    @FXML private TableColumn<Report, String>  month_count;
    @FXML private TableColumn<Report, String> user;
    @FXML private TableColumn<Report, String>  user_count;
    @FXML private TableView<Report> month_report_table;
    @FXML private TableView<Report> type_report_table;
    @FXML private TableView<Report> user_report_table;
    public static ObservableList<Report> allUserReports = FXCollections.observableArrayList();
    public static ObservableList<Report> allTypeReports = FXCollections.observableArrayList();
    public static ObservableList<Report> allMonthReports = FXCollections.observableArrayList();


    String monthQuery = "select count(MONTH(start)) as count, MONTH(start) as month from appointments a group by MONTH(start);";
    String typeQuery ="select count(type) as count, type from appointments group by type;";
    String userQuery = "select count(User_ID) as count, User_ID from appointments group by User_ID;";
    static ResourceBundle text;

    public void initialize() {
        Helper.connectToAndQueryDatabase();
        Locale currentLocale = Locale.getDefault();
        text = ResourceBundle.getBundle("TextBundle", currentLocale);
        type_report.setText(text.getString("type_report_label"));
        month_report.setText(text.getString("month_report_label"));
        user_report.setText(text.getString("user_report_label"));
        type.setText(text.getString("type"));
        type_count.setText(text.getString("count"));
        month.setText(text.getString("month"));
        month_count.setText(text.getString("count"));
        user.setText(text.getString("user_id"));
        user_count.setText(text.getString("count"));
        populateMonthTable();
        populateTypeTable();
        populateUserTable();
    }

    public void populateMonthTable() {
        allMonthReports.clear();
        try (Statement stmt = Helper.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(monthQuery);
            while (rs.next()) {
                Report report = new Report (
                    rs.getString("count"),
                    rs.getString("month")
                );
                allMonthReports.add(report);
            }
            month_report_table.setItems(allMonthReports);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
    public void populateTypeTable() {
        allTypeReports.clear();
        try (Statement stmt = Helper.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(typeQuery);
            while (rs.next()) {
                Report report = new Report (
                        rs.getString("count"),
                        rs.getString("type")
                );
                allTypeReports.add(report);
            }
            type_report_table.setItems(allTypeReports);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
    public void populateUserTable() {
        allUserReports.clear();
        try (Statement stmt = Helper.con.createStatement()) {
            ResultSet rs = stmt.executeQuery(userQuery);
            while (rs.next()) {
                Report report = new Report (
                        rs.getString("count"),
                        rs.getString("User_ID")
                );
                allUserReports.add(report);
            }
            user_report_table.setItems(allUserReports);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
