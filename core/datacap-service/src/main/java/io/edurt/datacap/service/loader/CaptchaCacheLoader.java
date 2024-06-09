package io.edurt.datacap.service.loader;

import com.google.common.cache.CacheLoader;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.captcha.entity.ResultEntity;

@SuppressFBWarnings(value = {"NP_NONNULL_RETURN_VIOLATION"})
public class CaptchaCacheLoader
        extends CacheLoader<Long, ResultEntity>
{
    @Override
    public ResultEntity load(Long key)
    {
        return null;
    }
}
