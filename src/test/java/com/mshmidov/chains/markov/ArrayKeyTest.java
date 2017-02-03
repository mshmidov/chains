package com.mshmidov.chains.markov;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayKeyTest {

    @Test
    @DisplayName("should new element to the key")
    public void shouldAppendElement() throws Exception {
        // given
        final ArrayKey<Character> key = new ArrayKey<>(new Character[] {'a', 'b', 'c'});

        // when
        final Key<Character> newKey = key.append('d');

        // then
        assertAll("new key",
                () -> assertWithMessage("should append new element and shift one element left")
                        .that(newKey.values()).isEqualTo(new Character[] {'b', 'c', 'd'}),
                () -> assertEquals(key.order(), newKey.order(), "should have the same order as old one")
        );

        assertAll("old key",
                () -> assertWithMessage("should remain unchanged").that(key.values()).isEqualTo(new Character[] {'a', 'b', 'c'})
        );

    }
}
