package io.edurt.datacap.server.converter;

import io.edurt.datacap.server.common.JSON;

import javax.persistence.AttributeConverter;

import java.util.Properties;

public class PropertiesConverter
        implements AttributeConverter<Properties, String>
{
    @Override
    public String convertToDatabaseColumn(Properties properties)
    {
        return JSON.toJSON(properties);
    }

    @Override
    public Properties convertToEntityAttribute(String s)
    {
        return JSON.toProperties(s);
    }
}
