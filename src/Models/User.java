package Models;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    public int id;
    public String user_name;
    public String password;
    public LocalDate create_date;
    public String created_by;
    public LocalDate update_date;
    public String updated_by;

    public User(int id, String user_name, LocalDate create_date, String created_by, LocalDate update_date, String updated_by, String password) throws ParseException {
        System.out.println(id + user_name + create_date + created_by + update_date + updated_by + password);
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.create_date = create_date;
        this.created_by = created_by;
        this.update_date = update_date;
        this.updated_by = updated_by;
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
    public String getUserName() {
        return user_name;
    }

    /**
     * @param name the name to set
     */
    public void setUserName(String name) {
        this.user_name = name;
    }

    @Override
    public String toString() {
        return getUserName();
    }
}
