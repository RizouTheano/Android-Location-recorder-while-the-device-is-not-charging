package gr.hua.dit.android.assignment2;

public class ContactContract {
    private String longitude;
    private String latitude;
    private String timestamp;
    public ContactContract(){}

    public ContactContract(String timestamp, String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;

    }
    public String getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}

