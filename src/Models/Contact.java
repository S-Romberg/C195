package Models;

public class Contact {
    public int id;
    public String name;
    public String email;

    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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
    public String getName() {
        return name;
    }

    /**
     * @param name sets name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email sets email
     */
    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return getName();
    }

}
