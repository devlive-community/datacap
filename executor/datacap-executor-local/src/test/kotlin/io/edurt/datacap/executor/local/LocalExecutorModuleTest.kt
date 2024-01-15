package io.edurt.datacap.executor.local

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.executor.Executor
import io.edurt.datacap.executor.ExecutorManager
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class LocalExecutorModuleTest {
    private val name = "Default"
    private var injector: Injector? = null

    @Before
    fun before() {
        injector = Guice.createInjector(ExecutorManager())
    }

    @Test
    fun test() {
        val executor: Executor? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Executor>>() {}))
                ?.first { it.name() == name }
        assertNotNull(executor)
    }
}
