package io.edurt.datacap.plugin.timescale

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class TimescaleContainer : GenericContainer<TimescaleContainer> {
    constructor() : super(DockerImageName.parse(DEFAULT_IMAGE_NAME)) {
        setupContainer()
    }

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        setupContainer()
    }

    private fun setupContainer() {
        withExposedPorts(PORT, DOCKER_PORT)
        withEnv("POSTGRES_PASSWORD", PASSWORD)
        withEnv("POSTGRES_DB", DATABASE)
    }

    companion object {
        private const val DEFAULT_IMAGE_NAME = "timescale/timescaledb-ha:pg16"

        const val PORT = 5432
        const val DOCKER_PORT = 5432
        const val PASSWORD = "postgres"
        const val DATABASE = "postgres"
    }
}
