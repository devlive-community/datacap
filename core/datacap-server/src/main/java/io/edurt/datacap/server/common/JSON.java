package io.edurt.datacap.server.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JSON
{
    public static final ObjectMapper objectmapper = new ObjectMapper();

    private JSON() {}

    public static String toJSON(Object object)
    {
        String json;
        try {
            json = objectmapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            json = null;
        }
        return json;
    }

    public static Map<String, Object> toMap(String json)
    {
        Map<String, Object> map;
        try {
            map = objectmapper.readValue(json, Map.class);
        }
        catch (JsonProcessingException e) {
            map = null;
        }
        return map;
    }
}
