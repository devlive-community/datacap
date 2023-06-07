package io.edurt.datacap.spi.adapter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.column.JdbcColumn;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.formatter.FormatterFactory;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class JdbcAdapter
        implements Adapter
{
    protected JdbcConnection jdbcConnection;

    public JdbcAdapter(JdbcConnection jdbcConnection)
    {
        this.jdbcConnection = jdbcConnection;
    }

    public Object handlerFormatter(FormatType format, List<String> headers, List<Object> columns)
    {
        return FormatterFactory.createFormatter(format, headers, columns).formatter();
    }

    @Override
    public Response handlerExecute(String content)
    {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.jdbcConnection.getResponse();
        Connection connection = (Connection) this.jdbcConnection.getConnection();
        JdbcConfigure configure = (JdbcConfigure) this.jdbcConnection.getConfigure();
        if (response.getIsConnected()) {
            try (PreparedStatement statement = connection.prepareStatement(content)) {
                List<String> headers = new ArrayList<>();
                List<String> types = new ArrayList<>();
                List<Object> columns = new ArrayList<>();
                try (ResultSet resultSet = statement.executeQuery()) {
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
                        columns.add(handlerFormatter(configure.getFormat(), headers, _columns));
                    }
                }
                catch (SQLException tryUpdateEx) {
                    if (Objects.equals(tryUpdateEx.getSQLState(), "S1009")) {
                        try {
                            headers.add("result");
                            types.add(Integer.class.getSimpleName());
                            List<Object> _columns = new ArrayList<>();
                            _columns.add(statement.executeUpdate());
                            columns.add(handlerFormatter(configure.getFormat(), headers, _columns));
                        }
                        catch (SQLException updateEx) {
                            throw new SQLException(updateEx);
                        }
                    }
                    else {
                        throw new SQLException(tryUpdateEx);
                    }
                }
                finally {
                    response.setHeaders(headers);
                    response.setTypes(types);
                    response.setColumns(columns);
                    response.setIsSuccessful(Boolean.TRUE);
                }
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
        this.jdbcConnection.destroy();
        return response;
    }
}
