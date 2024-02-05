package io.edurt.datacap.plugin.paradedb

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class ParadeDBContainer : GenericContainer<ParadeDBContainer> {
    constructor() : super(DockerImageName.parse(DEFAULT_IMAGE_NAME)) {
        setupContainer()
    }

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        setupContainer()
    }

    private fun setupContainer() {
        withExposedPorts(PORT, DOCKER_PORT)
        withEnv("POSTGRES_USER", USERNAME)
        withEnv("POSTGRES_PASSWORD", PASSWORD)
        withEnv("POSTGRES_DB", DATABASE)
    }

    companion object {
        private const val DEFAULT_IMAGE_NAME = "paradedb/paradedb:latest"

        const val PORT = 5432
        const val DOCKER_PORT = 5432
        const val USERNAME = "postgres"
        const val PASSWORD = "postgres"
        const val DATABASE = "postgres"
    }
}
