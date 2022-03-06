package Models;

/**
 * Represents a division, has an id, country, and division (string, not class)
 */
public class Division {
    public int id;
    public String country;
    public String division;

    public Division(int id, String country, String division) {
        this.id = id;
        this.country = country;
        this.division = division;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }


    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country sets country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division sets division
     */
    public void setDivision(String division) {
        this.division = division;
    }

}
