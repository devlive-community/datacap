package io.edurt.datacap.common.utils;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.fs.Fs;

import java.util.Optional;
import java.util.Set;

public class SpiUtils
{
    private SpiUtils()
    {
    }

    /**
     * Finds a specific Fs object by name.
     *
     * @param injector the injector used for dependency injection
     * @param name the name of the Fs object to find
     * @return an Optional containing the found Fs object, or an empty Optional if not found
     */
    public static Optional<Fs> findFs(Injector injector, String name)
    {
        Optional<Fs> optionalFs = injector.getInstance(Key.get(new TypeLiteral<Set<Fs>>() {}))
                .stream()
                .filter(item -> item.name().equalsIgnoreCase(name))
                .findFirst();
        return optionalFs;
    }
}
