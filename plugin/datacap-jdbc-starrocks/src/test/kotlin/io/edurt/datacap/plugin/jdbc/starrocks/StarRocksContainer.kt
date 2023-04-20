package io.edurt.datacap.plugin.jdbc.starrocks

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class StarRocksContainer : GenericContainer<StarRocksContainer?> {
    constructor() : super(DEFAULT_IMAGE_NAME)

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        withExposedPorts(BE_PORT, WEBSERVER_PORT, HEARTBEAT_SERVICE_PORT, BRPC_PORT, HTTP_PORT, RPC_PORT, QUERY_PORT, EDIT_LOG_PORT, BROKER_IPC_PORT)
    }

    companion object {
        private val DEFAULT_IMAGE_NAME = DockerImageName.parse("starrocks:2.2.12")
        const val BE_PORT = 9060
        const val WEBSERVER_PORT = 8040
        const val HEARTBEAT_SERVICE_PORT = 9050
        const val BRPC_PORT = 8060
        const val HTTP_PORT = 8030
        const val RPC_PORT = 9020
        const val QUERY_PORT = 9030
        const val EDIT_LOG_PORT = 9010
        const val BROKER_IPC_PORT = 8000
    }
}