package no.helsenorge.nipen.heartRate;

import java.sql.Timestamp;

public class HeartRate {

    private long id;
    private long  userId;
    private Timestamp timestamp;
    private long value;
    private String unit;

    public HeartRate(long id, long value, Timestamp timestamp, String unit) {
        this.id = id;
        //this.userId = userId;
        this.timestamp = timestamp;
        this.value = value;
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    /*public long getUserId() {
        return userId;
    }*/

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public long getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
