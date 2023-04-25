package io.edurt.datacap.jdbc.hologres

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class HologresContainer : GenericContainer<HologresContainer?> {
    constructor() : super(DEFAULT_IMAGE_NAME)

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        withExposedPorts(RPC_PORT)
    }

    companion object {
        private val DEFAULT_IMAGE_NAME = DockerImageName.parse("postgres:latest")
        const val RPC_PORT = 5432
    }
}