package io.edurt.datacap.convert

import com.google.inject.Guice
import org.junit.Assert
import org.junit.Test

class ConvertFilterTest
{
    private val injector = Guice.createInjector(ConvertManager())

    @Test
    fun testFilter()
    {
        Assert.assertNotNull(
            ConvertFilter.filter(injector, "Test")
                .get()
        )
    }
}
