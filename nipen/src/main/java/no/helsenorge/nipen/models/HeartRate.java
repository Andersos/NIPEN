package no.helsenorge.nipen.models;

import org.codehaus.jackson.annotate.JsonIgnore;

public class HeartRate {

    @JsonIgnore
    private int id;

    private int userId;
    private int value;
    private String timestamp;
    private String unit;

    /* DO NOT REMOVE */
    public HeartRate() {
        ;
    }

    public HeartRate(int userId, int value, String timestamp, String unit) {
        this.userId = userId;
        this.value = value;
        this.timestamp = timestamp;
        this.unit = unit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public int getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
