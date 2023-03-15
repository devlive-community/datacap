package io.edurt.datacap.server.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Properties;

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

    public static <T> T toObject(String json, Class<T> clazz)
    {
        T properties;
        try {
            properties = objectmapper.readValue(json, clazz);
        }
        catch (JsonProcessingException e) {
            properties = null;
        }
        return properties;
    }

    public static Properties toProperties(String json)
    {
        return toObject(json, Properties.class);
    }
}
