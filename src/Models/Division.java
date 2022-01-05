package Models;

public class Division {
    public String country;
    public String division;

    public Division(String country, String division) {
        this.country = country;
        this.division = division;
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
