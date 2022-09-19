package io.edurt.datacap.plugin.jdbc.mysql;

import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MySQLProcessor
{
    private final MySQLConnection connection;

    public MySQLProcessor(MySQLConnection connection)
    {
        this.connection = connection;
    }

    public Response handlerExecute(String content)
    {
        Response response = new Response();
        try (Statement statement = this.connection.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(content)) {
            List<String> headers = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<List<Object>> columns = new ArrayList<>();
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
                columns.add(_columns);
            }
            response.setHeaders(headers);
            response.setTypes(types);
            response.setColumns(columns);
        }
        catch (SQLException ex) {
            log.error("Execute content failed content {} exception {}", content);
        }
        return response;
    }
}
