package Models;

public class Report {
    public String count;
    public String detail;

    public Report(String count, String detail) {
        this.count = count;
        this.detail = detail;
    }

    /**
     * @return the count
     */
    public String getCount() {
        return count;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

}
