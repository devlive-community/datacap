package io.edurt.datacap.server.authorize;

public class UserNotEqualsException
        extends Exception
{
    public UserNotEqualsException()
    {
        super();
    }

    public UserNotEqualsException(String message)
    {
        super(message);
    }
}
