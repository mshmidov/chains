package com.isabaka.chains.util;

import java.util.function.Function;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StringIterator")
public class StringIteratorTest {

    @Test
    @DisplayName("should iterate a string by character")
    public void shouldIterateStringByCharacter() {

        // given
        final String string = "o234502340fjsd.mc.,azmd/sakedowec5u w";
        final StringIterator<Character> stringIterator = new StringIterator<>(string, Function.identity());

        // when
        final String result = Joiner.on("").join(stringIterator);

        // then
        assertThat(result).isEqualTo(string);

    }
}
