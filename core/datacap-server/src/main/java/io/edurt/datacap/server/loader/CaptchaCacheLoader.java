package io.edurt.datacap.server.loader;

import com.google.common.cache.CacheLoader;
import io.edurt.datacap.captcha.entity.ResultEntity;

public class CaptchaCacheLoader
        extends CacheLoader<Long, ResultEntity>
{
    @Override
    public ResultEntity load(Long key)
    {
        return null;
    }
}
