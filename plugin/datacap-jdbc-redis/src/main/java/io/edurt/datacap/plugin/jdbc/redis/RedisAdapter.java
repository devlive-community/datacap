package io.edurt.datacap.plugin.jdbc.redis;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.column.JdbcColumn;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class RedisAdapter
        extends JdbcAdapter
{
    public RedisAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }

    @Override
    public Response handlerExecute(String content)
    {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.connection.getResponse();
        Connection connection = (Connection) this.connection.getConnection();
        JdbcConfigure configure = (JdbcConfigure) this.connection.getConfigure();
        if (response.getIsConnected()) {
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(content)) {
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
                        _columns.add(jdbcColumn.mappingColumnData(metaData.getColumnTypeName(i), i));
                    }
                    isPresent = false;
                    columns.add(handlerFormatter(configure.getFormat(), headers, _columns));
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
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        // It will be destroyed after the mission is closed
        this.connection.destroy();
        return response;
    }
}
