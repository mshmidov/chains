package com.isabaka.chains.markov;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyTest {

    @Test
    @DisplayName("should append new element to the key")
    public void shouldAppendElement() throws Exception {
        // given
        final Key<Character> key = new Key<>('a', 'b', 'c');

        // when
        final Key<Character> newKey = key.append('d');

        // then
        assertThat(newKey.values()).as("should append new element and shift one element left").containsExactly('b', 'c', 'd');

        assertEquals(key.order(), newKey.order(), "should have the same order as old one");

        assertThat(key.values()).as("should remain unchanged").containsExactly('a', 'b', 'c');

    }

}
