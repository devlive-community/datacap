package io.edurt.datacap.convert

import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import java.util.*

object ConvertFilter
{
    /**
     * Finds a file in the injector by name.
     *
     * @param injector the injector to search in
     * @param name the name of the file to find
     * @return an Optional containing the found File, or an empty Optional if not found
     */
    @JvmStatic
    fun filter(injector: Injector, name: String): Optional<Convert>
    {
        return injector.getInstance(Key.get(object : TypeLiteral<Set<Convert>>()
        {}))
            .stream()
            .filter { it.name() == name }
            .findFirst()
    }
}
