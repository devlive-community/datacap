package io.edurt.datacap.plugin.jdbc.mongo;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class MongoAdapter
        extends JdbcAdapter
{
    public MongoAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
