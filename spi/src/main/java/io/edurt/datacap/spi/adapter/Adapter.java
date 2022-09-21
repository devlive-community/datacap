package io.edurt.datacap.spi.adapter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.record.RecordFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"},
        justification = "I prefer to suppress these FindBugs warnings")
public abstract class Adapter
{
    private Object handlerFormatAdapterRecord(FormatType format, List<String> headers, List<Object> columns)
    {
        return RecordFactory.createRecord(format, headers, columns).convert();
    }

    public Response handlerJDBCExecute(FormatType format, String content, Connection connection, Response response)
    {
        if (response.getIsConnected()) {
            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(content)) {
                List<String> headers = new ArrayList<>();
                List<String> types = new ArrayList<>();
                List<Object> columns = new ArrayList<>();
                boolean isPresent = true;
                while (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    List<Object> _columns = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        if (isPresent) {
                            headers.add(metaData.getColumnName(i));
                            types.add(metaData.getColumnTypeName(i));
                        }
                        _columns.add(resultSet.getString(i));
                    }
                    isPresent = false;
                    columns.add(handlerFormatAdapterRecord(format, headers, _columns));
                }
                response.setHeaders(headers);
                response.setTypes(types);
                response.setColumns(columns);
                response.setIsSuccessful(Boolean.TRUE);
            }
            catch (SQLException ex) {
                log.error("Execute content failed content {} exception ", content, ex);
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            }
        }
        return response;
    }
}
