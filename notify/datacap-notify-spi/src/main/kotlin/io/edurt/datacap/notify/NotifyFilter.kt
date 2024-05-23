package io.edurt.datacap.notify

import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import java.util.*

object NotifyFilter
{
    /**
     * Finds a Notify object by name using the given injector.
     *
     * @param injector the Guice injector used for dependency injection
     * @param name the name of the Notify object to find
     * @return an Optional containing the found Notify object, or an empty Optional if not found
     */
    @JvmStatic
    fun findNotify(injector: Injector, name: String): Optional<Notify>
    {
        return injector.getInstance(Key.get(object : TypeLiteral<Set<Notify>>()
        {}))
            .stream()
            .filter { it.name() == name }
            .findFirst()
    }
}
