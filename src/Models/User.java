package Models;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

/**
 * Represents a user
 */
public class User {
    public int id;
    public String user_name;
    public String password;
    public LocalDateTime create_date;
    public String created_by;
    public LocalDateTime update_date;
    public String updated_by;

    public User(int id, String user_name, LocalDateTime create_date, String created_by, LocalDateTime update_date, String updated_by, String password) throws ParseException {
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.create_date = create_date;
        this.created_by = created_by;
        this.update_date = update_date;
        this.updated_by = updated_by;
    }

    /**
     * @return users id
     */
    public int getId() {
        return id;
    }

    /**
     * @return users name
     */
    public String getUserName() {
        return user_name;
    }

    /**
     * Sets user_name to new value
     * @param name the new name for user
     */
    public void setUserName(String name) {
        this.user_name = name;
    }

    @Override
    public String toString() {
        return getUserName();
    }
}
