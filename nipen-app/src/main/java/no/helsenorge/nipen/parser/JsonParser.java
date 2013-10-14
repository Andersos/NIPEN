package no.helsenorge.nipen.parser;

import no.helsenorge.nipen.exception.JsonInputException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    public static Map<String, Object> jsonToHashMap(String json) {
        Map<String, Object> parseResult = new HashMap<String, Object>();

        try {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            parseResult = mapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new JsonInputException(e);
        }

        return parseResult;
    }
}
