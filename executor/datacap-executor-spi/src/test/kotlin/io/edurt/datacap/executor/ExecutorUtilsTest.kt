package io.edurt.datacap.executor

import com.google.inject.Guice
import com.google.inject.Injector
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class ExecutorUtilsTest {
    private var injector: Injector? = null

    @Before
    fun before() {
        injector = Guice.createInjector(ExecutorManager())
    }

    @Test
    fun test() {
        assertNotNull(injector?.let { ExecutorUtils.findOne(it, "Test") })
    }
}
