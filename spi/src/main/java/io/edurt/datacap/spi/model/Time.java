package io.edurt.datacap.spi.model;

import lombok.Getter;
import lombok.Setter;

public class Time
{
    @Getter
    @Setter
    private long start;
    @Getter
    private long end;
    @Getter
    private long elapsed;

    public void setEnd(long end)
    {
        this.end = end;
        this.elapsed = end - start;
    }
}
