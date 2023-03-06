package io.edurt.datacap.core;

import java.util.Arrays;

public class Logger
{
    private final Class<?> mark;

    public Logger(Class<?> mark)
    {
        this.mark = mark;
    }

    public synchronized void log(String format, Object... arguments)
    {
        Object[] objs = Arrays.stream(arguments)
                .map(t -> {
                    if (t instanceof Throwable) {
                        Throwable th = (Throwable) t;
                        return th.getMessage();
                    }
                    return t;
                }).toArray(Object[]::new);
        System.out.printf(mark + ":::" + format + "\n", objs);
    }
}
