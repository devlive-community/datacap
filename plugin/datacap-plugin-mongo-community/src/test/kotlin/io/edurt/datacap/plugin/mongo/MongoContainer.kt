package io.edurt.datacap.plugin.mongo

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.DockerImageName.parse

class MongoContainer : GenericContainer<MongoContainer> {
    constructor() : super(DEFAULT_IMAGE_NAME)

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        withExposedPorts(PORT)
    }

    companion object {
        private val DEFAULT_IMAGE_NAME = parse("mongo:latest")

        const val PORT = 27017
    }
}