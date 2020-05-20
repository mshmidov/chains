package com.isabaka.chains.util;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.isabaka.chains.util.StringIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

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
        Truth.assertThat(result).isEqualTo(string);

    }
}
