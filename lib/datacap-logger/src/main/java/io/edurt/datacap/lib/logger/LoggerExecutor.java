package io.edurt.datacap.lib.logger;

public abstract class LoggerExecutor<T>
{
    protected String directory;
    protected String name;

    public LoggerExecutor(final String directory, final String name)
    {
        this.directory = directory;
        this.name = name;
    }

    public abstract T getLogger();

    public abstract void destroy();
}
