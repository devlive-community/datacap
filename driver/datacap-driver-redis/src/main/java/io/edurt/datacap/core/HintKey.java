package io.edurt.datacap.core;

import java.util.Arrays;
import java.util.Objects;

public enum HintKey {
    decoder,
    sample_key,
    noop;

    public static HintKey fromString(String string) {
        return Arrays.stream(values())
                .filter(t -> Objects.equals(t.toString(), string))
                .findFirst()
                .orElse(noop);
    }
}
