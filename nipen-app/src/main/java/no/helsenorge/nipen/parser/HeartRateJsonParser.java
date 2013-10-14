package no.helsenorge.nipen.parser;

import no.helsenorge.nipen.model.HeartRate;

public final class HeartRateJsonParser extends DataTypeParser {

    public HeartRateJsonParser(String json) {
        super(json);
    }

    public HeartRate toHeartRate(){
        long userId = readUserId();
        String timestamp = readTimestamp();
        int value = readValue();
        String unit = readUnit();

        return new HeartRate(userId, timestamp, value, unit);
    }

    private int readValue() {
        return (Integer)parseResult.get("value");
    }

    private String readUnit() {
        return (String)parseResult.get("unit");
    }
}
