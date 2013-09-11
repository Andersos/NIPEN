package main.java.no.helsenorge.nipen.heart_rate;

import java.sql.Timestamp;

public class HeartRate {

    private long id;
    private long  userId;
    private Timestamp timestamp;
    private long value;

    public HeartRate(long id, long userId, Timestamp timestamp, long value){
        this.id = id;
        this.userId = userId;
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public long getValue() {
        return value;
    }
}
