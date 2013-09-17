package no.helsenorge.nipen.heartRate;

import java.sql.Timestamp;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

public final class HeartRateJsonParser {
    private String json;
    private Map<String,Object> parseResult;

    public HeartRateJsonParser(String json) {
        this.json = json;
    }

    public HeartRate toHeartRate(){
        jsonToHashMap();
        long id = readId();
        Timestamp timestamp = readTimestamp();
        long value = readValue();
        String unit = readUnit();

        return new HeartRate(id, value, timestamp, unit);
    }

    private long readId() {

        return ((Integer)parseResult.get("id")).intValue();
    }

    private Timestamp readTimestamp() {
        return ((Timestamp)parseResult.get("timestamp"));
    }

    private long readValue() {
        return ((Integer)parseResult.get("value")).intValue();
    }

    private String readUnit() {
        return ((String)parseResult.get("id"));
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
