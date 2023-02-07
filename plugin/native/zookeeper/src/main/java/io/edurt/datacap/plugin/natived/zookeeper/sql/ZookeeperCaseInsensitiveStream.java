package io.edurt.datacap.plugin.natived.zookeeper.sql;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.misc.Interval;

public class ZookeeperCaseInsensitiveStream
        implements CharStream
{
    private final CharStream stream;

    public ZookeeperCaseInsensitiveStream(CharStream stream)
    {
        this.stream = stream;
    }

    @Override
    public String getText(Interval interval)
    {
        return stream.getText(interval);
    }

    @Override
    public void consume()
    {
        stream.consume();
    }

    @Override
    public int LA(int i)
    {
        int result = stream.LA(i);

        switch (result) {
            case 0:
            case IntStream.EOF:
                return result;
            default:
                return Character.toUpperCase(result);
        }
    }

    @Override
    public int mark()
    {
        return stream.mark();
    }

    @Override
    public void release(int marker)
    {
        stream.release(marker);
    }

    @Override
    public int index()
    {
        return stream.index();
    }

    @Override
    public void seek(int index)
    {
        stream.seek(index);
    }

    @Override
    public int size()
    {
        return stream.size();
    }

    @Override
    public String getSourceName()
    {
        return stream.getSourceName();
    }
}
