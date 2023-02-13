package io.edurt.datacap.server.service.impl;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.service.FormatService;
import org.springframework.stereotype.Service;

@Service
public class FormatServiceImpl
        implements FormatService
{
    @Override
    public Response<String> formatterSql(String sql)
    {
        String formatterSql = SqlFormatter.of(Dialect.MySql)
                .format(sql);
        return Response.success(formatterSql);
    }
}
