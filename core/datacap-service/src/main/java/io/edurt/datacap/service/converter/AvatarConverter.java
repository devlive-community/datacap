package io.edurt.datacap.service.converter;

import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.service.entity.convert.AvatarEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;

public class AvatarConverter
        implements AttributeConverter<AvatarEntity, String>
{
    @Override
    public String convertToDatabaseColumn(AvatarEntity entity)
    {
        return JsonUtils.toJSON(entity);
    }

    @Override
    public AvatarEntity convertToEntityAttribute(String s)
    {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return JsonUtils.toObject(s, AvatarEntity.class);
    }
}
