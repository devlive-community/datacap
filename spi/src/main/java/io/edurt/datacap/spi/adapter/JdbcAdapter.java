package io.edurt.datacap.spi.adapter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.column.JdbcColumn;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
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
public class JdbcAdapter
        implements Adapter
{
    private JdbcConnection jdbcConnection;

    public JdbcAdapter(JdbcConnection jdbcConnection)
    {
        this.jdbcConnection = jdbcConnection;
    }

    private Object handlerFormatAdapterRecord(FormatType format, List<String> headers, List<Object> columns)
    {
        return RecordFactory.createRecord(format, headers, columns).convert();
    }

    @Override
    public Response handlerJDBCExecute(String content)
    {
        Response response = this.jdbcConnection.getResponse();
        Connection connection = this.jdbcConnection.getConnection();
        JdbcConfigure configure = this.jdbcConnection.getConfigure();
        if (response.getIsConnected()) {
            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(content)) {
                List<String> headers = new ArrayList<>();
                List<String> types = new ArrayList<>();
                List<Object> columns = new ArrayList<>();
                boolean isPresent = true;
                JdbcColumn jdbcColumn = new JdbcColumn(resultSet);
                while (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    List<Object> _columns = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        if (isPresent) {
                            headers.add(metaData.getColumnName(i));
                            types.add(metaData.getColumnTypeName(i));
                        }
                        _columns.add(jdbcColumn.convert(metaData.getColumnTypeName(i), i));
                    }
                    isPresent = false;
                    columns.add(handlerFormatAdapterRecord(configure.getFormat(), headers, _columns));
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
        // It will be destroyed after the mission is closed
        this.jdbcConnection.destroy();
        return response;
    }
}
