package io.edurt.datacap.schedule

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ScheduleManagerTest {
    private var injector: Injector? = null

    @Before
    fun before() {
        injector = Guice.createInjector(ScheduleManager())
    }

    @Test
    fun test() {
        assertNotNull(injector?.getInstance(Key.get(object : TypeLiteral<Set<Schedule?>?>() {})))
    }
}
