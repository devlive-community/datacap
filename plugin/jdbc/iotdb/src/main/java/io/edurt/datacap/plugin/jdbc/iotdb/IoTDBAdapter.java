package io.edurt.datacap.plugin.jdbc.iotdb;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class IoTDBAdapter
        extends JdbcAdapter
{
    public IoTDBAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
