package no.helsenorge.nipen.parser;

import java.util.Map;

public class DataTypeParser {
    protected String json;
    protected Map<String,Object> parseResult;

    protected DataTypeParser(String json) {
        this.json = json;
        parseResult = JsonParser.jsonToHashMap(json);
    }

    protected long readUserId() {
        return (Integer)parseResult.get("userId");  // FIXME: WHY CAN'T THIS BE CAST TO LONG!?!?!?!?
    }

    protected String readTimestamp() {
        return (String)parseResult.get("timestamp");
    }
}
