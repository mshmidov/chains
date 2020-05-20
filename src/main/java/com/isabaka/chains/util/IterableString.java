package com.isabaka.chains.util;

import java.util.function.Function;

public final class IterableString {

    private IterableString() {
    }

    public static Iterable<Character> asCharacters(final String s) {
        return () -> new StringIterator<>(s, Function.identity());
    }

    public static Iterable<String> asStrings(final String s) {
        return () -> new StringIterator<>(s, Object::toString);
    }


}
