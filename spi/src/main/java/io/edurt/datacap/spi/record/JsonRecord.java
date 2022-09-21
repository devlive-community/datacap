package io.edurt.datacap.spi.record;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class JsonRecord
        extends Record
{
    protected JsonRecord(List<String> headers, List<Object> columns)
    {
        super(headers, columns);
    }

    @Override
    public Object convert()
    {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        for (int i = 0; i < headers.size(); i++) {
            node.put(headers.get(i), String.valueOf(columns.get(i)));
        }
        return node;
    }
}
