package com.mshmidov.chains.chain;


import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayKeyTest {

    @Test
    @DisplayName("when appending new element to the key")
    public void shouldAppendElement() throws Exception {
        // given
        final ArrayKey<Character> key = new ArrayKey<>(new Character[] {'a', 'b', 'c'});

        // when
        final Key<Character> newKey = key.append('d');

        // then
        assertAll("new key",
                () -> assertEquals(ImmutableList.of('b', 'c', 'd'), newKey.values(), "should append new element and shift one element left"),
                () -> assertEquals(key.order(), newKey.order(), "should have the same order as old one")
        );

        assertAll("old key",
                () -> assertEquals(ImmutableList.of('a', 'b', 'c'), key.values(), "should remain unchanged")
        );

    }
}
