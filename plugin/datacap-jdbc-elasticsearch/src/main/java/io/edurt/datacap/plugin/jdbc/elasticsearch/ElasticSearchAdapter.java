package io.edurt.datacap.plugin.jdbc.elasticsearch;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class ElasticSearchAdapter
        extends JdbcAdapter
{
    public ElasticSearchAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
