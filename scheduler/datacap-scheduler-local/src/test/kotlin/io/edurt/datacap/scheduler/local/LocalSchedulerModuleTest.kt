package io.edurt.datacap.scheduler.local

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.scheduler.ScheduleManager
import io.edurt.datacap.scheduler.Scheduler
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class LocalSchedulerModuleTest {
    private val name = "Local"
    private var injector: Injector? = null

    @Before
    fun before() {
        injector = Guice.createInjector(ScheduleManager())
    }

    @Test
    fun test() {
        val scheduler: Scheduler? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Scheduler?>?>() {}))
                ?.first { v -> v?.name().equals(name) }
        assertNotNull(scheduler)
    }
}
