package io.edurt.datacap.plugin.jdbc.doris

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class DorisContainer : GenericContainer<DorisContainer?> {
    constructor() : super(DEFAULT_IMAGE_NAME) {
//        withExposedPorts(PORT, DOCKER_PORT)
    }

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        withExposedPorts(PORT, DOCKER_PORT)
    }

    companion object {
        private val DEFAULT_IMAGE_NAME = DockerImageName.parse("zykkk/doris:1.2.2.1-avx2-x86_84")
        const val PORT = 8961
        const val DOCKER_PORT = 9030
    }
}