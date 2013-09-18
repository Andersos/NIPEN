package no.helsenorge.nipen.heartRate;

import java.sql.Timestamp;

public class HeartRate {

    private long id = -1;
    private long  userId;
    private String timestamp;
    private long value;
    private String unit;

    public HeartRate(long userId, long value, String timestamp, String unit) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.value = value;
        this.unit = unit;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public long getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
