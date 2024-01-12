package io.edurt.datacap.matrixone

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class MatrixOneContainer : GenericContainer<MatrixOneContainer> {
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
        private const val DEFAULT_IMAGE_NAME = "matrixorigin/matrixone:1.1.0"

        const val PORT = 6001
        const val DOCKER_PORT = 6001
    }
}
