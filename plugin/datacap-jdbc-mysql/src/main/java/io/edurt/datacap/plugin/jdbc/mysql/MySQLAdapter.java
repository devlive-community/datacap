package io.edurt.datacap.plugin.jdbc.mysql;

import com.google.common.collect.Lists;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.column.JdbcColumn;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Slf4j
public class MySQLAdapter
        extends JdbcAdapter {
    public MySQLAdapter(JdbcConnection jdbcConnection) {
        super(jdbcConnection);
    }

    @Override
    public Response handlerExecute(String content) {
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
                // Split SQL
                List<String> setBuffer = Lists.newArrayList();
                List<String> buffer = Lists.newArrayList();
                // TODO Each line of code is required to have a semicolon; this is different from scala syntax without semicolons
                Arrays.stream(content.split(";"))
                        .forEach(line -> {
                            if (line.trim().toUpperCase().startsWith("SET")) {
                                setBuffer.add(line);
                            } else {
                                buffer.add(line);
                            }
                        });
                // Execute SET
                for (String sql : setBuffer) {
                    statement.execute(sql);
                }
                Statement statement1 = connection.createStatement();
                Boolean isSelect = false;
                ResultSet resultSet = null;
                System.out.println("buffer:" + buffer);
                for (String sql : buffer) {
                    if (sql.trim().toUpperCase().startsWith("SELECT")) {
                        resultSet = statement1.executeQuery(sql);
                        isSelect = true;
                        // If it is multiple result sets, return the first result set
                        break;
                    }
                    // High-risk operations such as delete 、truncate are not allowed!
                    if (sql.trim().toUpperCase().startsWith("INSERT")) {
                        statement1.executeUpdate(sql);
                        isSelect = true;
                        // If it is multiple result sets, return the first result set
                        break;
                    }
                    statement1.addBatch(sql);
                }
                if (!isSelect) {
                    statement1.executeBatch();
                    resultSet = statement1.getResultSet();
                }
                try {
                    // Reset query SQL
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
                } catch (SQLException tryUpdateEx) {
                    if (Objects.equals(tryUpdateEx.getSQLState(), "C1999")) {
                        try {
                            headers.add("result");
                            types.add(Integer.class.getSimpleName());
                            List<Object> _columns = new ArrayList<>();
                            _columns.add(statement.executeUpdate());
                            columns.add(handlerFormatter(configure.getFormat(), headers, _columns));
                        } catch (SQLException updateEx) {
                            throw new SQLException(updateEx);
                        }
                    } else {
                        throw new SQLException(tryUpdateEx);
                    }
                } finally {
                    response.setHeaders(headers);
                    response.setTypes(types);
                    response.setColumns(columns);
                    response.setIsSuccessful(Boolean.TRUE);
                }
            } catch (SQLException ex) {
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
