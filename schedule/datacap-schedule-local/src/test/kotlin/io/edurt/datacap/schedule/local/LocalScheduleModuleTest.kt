package io.edurt.datacap.schedule.local

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.schedule.Schedule
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class LocalScheduleModuleTest {
    private val name = "Local"
    private var injector: Injector? = null

    @Before
    fun before() {
        injector = Guice.createInjector(LocalScheduleModule())
    }

    @Test
    fun test() {
        val schedule: Schedule? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Schedule?>?>() {}))
                ?.first { v -> v?.name().equals(name) }
        assertNotNull(schedule)
    }
}
