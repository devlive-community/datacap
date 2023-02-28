package io.edurt.datacap.plugin.jdbc.neo4j;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class Neo4jAdapter
        extends JdbcAdapter
{
    public Neo4jAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
