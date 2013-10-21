package no.helsenorge.nipen.models;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Weight /*extends DataType*/ {

    @JsonIgnore
    private int id;

    private int userId;
    private int value;
    private String timestamp;
    private String unit;

    public Weight() {
        ;
    }

    public Weight(int userId, int value, String timestamp, String unit) {
        //setUserId(userId);
        //setTimestamp(timestamp);
        this.userId = userId;
        this.value = value;
        this.timestamp = timestamp;
        this.unit = unit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getValue() {
        return value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUnit() {
        return unit;
    }
}
