package io.edurt.datacap.lib.logger.logback;

import io.edurt.datacap.lib.logger.LoggerExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class LogbackExecutorTest
{
    private final String directory = System.getProperty("user.dir");
    private final String name = this.getClass().getName() + ".log";
    private LoggerExecutor loggerExecutor;
    private Logger logger;

    @Before
    public void before()
    {
        this.loggerExecutor = new LogbackExecutor(this.directory, this.name);
    }

    @Test
    public void getLogger()
    {
        logger = (Logger) loggerExecutor.getLogger();
        logger.info("info");
        logger.debug("debug");
        logger.error("error");
        logger.warn("warn");
        logger.trace("trace");
    }

    @Test
    public void destroy()
    {
        loggerExecutor.destroy();
        Assert.assertTrue(logger == null);
    }
}