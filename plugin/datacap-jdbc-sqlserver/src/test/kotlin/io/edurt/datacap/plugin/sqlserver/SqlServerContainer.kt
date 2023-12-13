package io.edurt.datacap.plugin.sqlserver

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class SqlServerContainer : GenericContainer<SqlServerContainer> {
    constructor() : super(DockerImageName.parse(IMAGE_2022_LATEST)) {
        setupContainer()
    }

    constructor(dockerImageName: DockerImageName) : super(dockerImageName) {
        dockerImageName.assertCompatibleWith(dockerImageName)
        setupContainer()
    }

    private fun setupContainer() {
        withExposedPorts(PORT, DOCKER_PORT)
        withEnv("ACCEPT_EULA", "Y")
        withEnv("MSSQL_SA_PASSWORD", PASSWORD)
    }

    companion object {
        const val IMAGE_2017_LATEST = "mcr.microsoft.com/mssql/server:2017-latest"
        const val IMAGE_2019_LATEST = "mcr.microsoft.com/mssql/server:2019-latest"
        const val IMAGE_2022_LATEST = "mcr.microsoft.com/mssql/server:2022-latest"
        const val PORT = 1433
        const val DOCKER_PORT = 1433
        const val PASSWORD = "community@Devlive.Org"
    }
}
