package io.edurt.datacap.plugin.pinot

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.DockerImageName.parse

class PinotContainer : GenericContainer<PinotContainer> {
    constructor() : super(DEFAULT_IMAGE_NAME)

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        withExposedPorts(PORT)
    }

    companion object {
        private val DEFAULT_IMAGE_NAME = parse("apachepinot/pinot:0.12.0")

        const val PORT = 9090
    }
}