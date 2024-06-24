package io.edurt.datacap.plugin.solr

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.DockerImageName.parse

class SolrContainer : GenericContainer<SolrContainer>
{
    constructor() : super(parse(DEFAULT_IMAGE_NAME))
    {
        setupContainer()
    }

    constructor(dockerImageName: DockerImageName) : super(dockerImageName)
    {
        dockerImageName.assertCompatibleWith(dockerImageName)
        setupContainer()
    }

    private fun setupContainer()
    {
        withExposedPorts(PORT, DOCKER_PORT)
    }

    companion object
    {
        private const val DEFAULT_IMAGE_NAME = "solr:9.6.1-slim"

        const val PORT = 8983
        const val DOCKER_PORT = 8983
    }
}
