package no.helsenorge.nipen.parser;

import no.helsenorge.nipen.model.Weight;

public class WeightJsonParser extends DataTypeParser {

    public WeightJsonParser(String json) {
        super(json);
    }

    public Weight toWeight(){
        long userId = readUserId();
        String timestamp = readTimestamp();
        int value = readValue();
        String unit = readUnit();

        return new Weight(userId, timestamp, value, unit);
    }

    private int readValue() {
        return (Integer)parseResult.get("value");
    }

    private String readUnit() {
        return (String)parseResult.get("unit");
    }
}
