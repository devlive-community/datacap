package io.edurt.datacap.service.converter;

import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.service.entity.itransient.user.UserEditorEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;

public class UserEditorConverter
        implements AttributeConverter<UserEditorEntity, String>
{
    @Override
    public String convertToDatabaseColumn(UserEditorEntity userEditorEntity)
    {
        return JsonUtils.toJSON(userEditorEntity);
    }

    @Override
    public UserEditorEntity convertToEntityAttribute(String s)
    {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        else {
            return JsonUtils.toObject(s, UserEditorEntity.class);
        }
    }
}
