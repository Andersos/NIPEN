package no.helsenorge.nipen.heartRate;

import java.sql.Timestamp;
import java.util.Map;

//import org.codehaus.jackson.JsonFactory;
//import org.codehaus.jackson.map.ObjectMapper;

public final class HeartRateJsonParser {
    private String json;
    private Map<String,Object> parseResult;

    public HeartRateJsonParser(String json) {
        this.json = json;
    }

    public HeartRate toHeartRate(){
        //jsonToHashMap();
        long id = readId();
        long userId = readUserId();
        Timestamp timestamp = readTimestamp();
        long value = readValue();
        String unit = readUnit();

        return new HeartRate(id, userId, timestamp, value, unit);
    }

    private long readId() {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    private long readUserId() {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    private Timestamp readTimestamp() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private long readValue() {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    private String readUnit() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    /*private void jsonToHashMap() {
        try {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            parseResult = mapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new JsonInputException(e);
        }
    }*/
}
