package io.edurt.datacap.spi.adapter;

import io.edurt.datacap.spi.model.Response;

public interface Adapter
{
    Response handlerJDBCExecute(String content);
}
