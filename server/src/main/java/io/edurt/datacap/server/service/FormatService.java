package io.edurt.datacap.server.service;

import io.edurt.datacap.server.common.Response;

public interface FormatService
{
    Response<String> formatterSql(String sql);
}
