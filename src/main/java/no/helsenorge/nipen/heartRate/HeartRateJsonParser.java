package no.helsenorge.nipen.heartRate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

public final class HeartRateJsonParser {
    private String json;
    private Map<String,Object> parseResult = new HashMap<String, Object>();

    public HeartRateJsonParser(String json) {
        this.json = json;
    }

    public HeartRate toHeartRate(){
        jsonToHashMap();
        long userId = readUserId();
        String timestamp = readTimestamp();
        long value = readValue();
        String unit = readUnit();

        return new HeartRate(userId, value, timestamp, unit);
    }

    private long readUserId() {
        return (Integer)parseResult.get("user_id");  // FIXME: WHY CAN'T THIS BE CAST TO LONG!?!?!?!?
    }

    private String readTimestamp() {
        return (String)parseResult.get("timestamp");
    }

    private long readValue() {
        return (Integer)parseResult.get("value");     // FIXME: WHY CAN'T THIS BE CAST TO LONG!?!?!?!?
    }

    private String readUnit() {
        return (String)parseResult.get("unit");
    }

    private void jsonToHashMap() {
        try {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            parseResult = mapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new JsonInputException(e);
        }
    }
}
