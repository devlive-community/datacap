package io.edurt.datacap.service.converter;

import com.google.common.collect.Lists;
import io.edurt.datacap.common.enums.DataSetState;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;

public class ListConverter
        implements AttributeConverter<List, String>
{
    @Override
    public String convertToDatabaseColumn(List map)
    {
        List<String> values = Lists.newArrayList();
        for (Object state : map) {
            values.add(state.toString());
        }
        return String.join(",", values);
    }

    @Override
    public List convertToEntityAttribute(String s)
    {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        else {
            return Lists.newArrayList(Arrays.stream(s.split(",")).map(DataSetState::valueOf).toArray(DataSetState[]::new));
        }
    }
}
