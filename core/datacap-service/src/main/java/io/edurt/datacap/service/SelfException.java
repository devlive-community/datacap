package io.edurt.datacap.service;

public class SelfException
        extends RuntimeException
{
    public SelfException()
    {
        super();
    }

    public SelfException(String message)
    {
        super(message);
    }
}
