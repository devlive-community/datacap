package io.edurt.datacap.plugin.scylladb

import com.datastax.oss.driver.api.core.CqlSession
import io.edurt.datacap.spi.connection.Connection
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import java.lang.Boolean
import java.net.InetSocketAddress
import kotlin.Exception
import kotlin.String
import kotlin.TODO

class ScyllaDBConnection : Connection {
    private var session: CqlSession? = null

    constructor(configure: Configure, response: Response) : super(configure, response)

    override fun formatJdbcUrl(): String {
        return TODO("Provide the return value")
    }

    override fun openConnection(): java.sql.Connection? {
        try {
            this.session = CqlSession.builder()
                    .addContactPoint(InetSocketAddress(configure?.host, configure !!.port))
                    .withLocalDatacenter(configure.database.orElse("datacenter"))
                    .build()
            response?.isConnected = Boolean.TRUE
        }
        catch (ex: Exception) {
            response?.isConnected = Boolean.FALSE
            response?.message = ex.message
        }
        return null
    }

    fun getSession(): CqlSession? {
        return session
    }

    override fun destroy() {
        this.session?.close()
    }
}
