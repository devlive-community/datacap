package io.edurt.datacap.scylladb

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class ScyllaDBContainer : GenericContainer<ScyllaDBContainer> {
    constructor() : super(DockerImageName.parse(DEFAULT_IMAGE_NAME)) {
        setupContainer()
    }

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        setupContainer()
    }

    private fun setupContainer() {
        withExposedPorts(PORT, DOCKER_PORT)
    }

    companion object {
        private const val DEFAULT_IMAGE_NAME = "scylladb/scylla"

        const val PORT = 9042
        const val DOCKER_PORT = 9042
    }
}
