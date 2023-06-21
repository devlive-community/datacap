package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;

public interface FormatService
{
    CommonResponse<String> formatterSql(String sql);
}
