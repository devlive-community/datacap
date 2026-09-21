package io.edurt.datacap.test.plugin

import io.edurt.datacap.plugin.Plugin
import io.edurt.datacap.plugin.loader.TarPluginLoader
import org.junit.Assume
import org.junit.BeforeClass
import org.junit.Test
import java.nio.file.Path
import kotlin.test.assertTrue

class TarPluginLoaderTest
{
    companion object
    {
        @BeforeClass
        @JvmStatic
        fun requireRemoteArtifact()
        {
            // 测试依赖从外部 CDN 下载 2024.4.0-SNAPSHOT 的历史构件，默认跳过保证 CI 稳定；
            // 需要时通过 -Ddatacap.test.remotePlugin=true 显式开启。
            Assume.assumeTrue("Remote CDN artifact is not available", java.lang.Boolean.getBoolean("datacap.test.remotePlugin"))
        }
    }

    private val tarUrl = "https://cdn.north.devlive.org/applications/datacap/plugins/2024.4.0-SNAPSHOT/convert/datacap-convert-txt-bin.tar.gz"

    @Test
    fun test()
    {
        val loader = TarPluginLoader()

        val plugins: List<Plugin> = loader.load(Path.of(tarUrl), setOf("io.edurt.datacap.plugin"))
        assertTrue(plugins.isNotEmpty())
    }
}
