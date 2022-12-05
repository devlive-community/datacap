package io.edurt.datacap.common;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class OptionalUtils
{
    private OptionalUtils() {}

    public static boolean isEmpty(Optional<String> optional)
    {
        boolean flag = false;
        if (optional.isPresent()) {
            if (ObjectUtils.isEmpty(optional.get()) && StringUtils.isEmpty(optional.get())) {
                flag = true;
            }
        }
        return flag;
    }

    public static boolean isNotEmpty(Optional<String> optional)
    {
        return !isEmpty(optional);
    }
}
