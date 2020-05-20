package com.isabaka.chains.markov;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("BoundedChainIterator")
public class BoundedChainIteratorTest {

    @Test
    @DisplayName("should not be created with illegal bound")
    public void shouldNotBeCreatedWithIllegalBound() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new BoundedChainIterator<>(BDDMockito.mock(MarkovChain.class), 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new BoundedChainIterator<>(BDDMockito.mock(MarkovChain.class), -1)));
    }

    @Test
    @DisplayName("should not be created without chain")
    public void shouldNotBeCreatedWithoutChain() {
        assertThrows(NullPointerException.class, () -> new BoundedChainIterator<>(null, 1));
    }

    @Test
    @DisplayName("should generate sequence up to terminal symbol")
    public void shouldGenerateSequenceUpToTerminalSymbol() {
        // given
        final String sequence = "abcdefg";
        final MarkovChain<String> chain = MarkovChainBuilder.newInstance(3, "z").populate(explode(sequence)).build();
        final BoundedChainIterator<String> iterator = new BoundedChainIterator<>(chain, 999);

        // when
        final String result = Joiner.on("").join(iterator);

        // then
        assertEquals(sequence, result);
    }

    @Test
    @DisplayName("should generate sequence up to desired limit")
    public void shouldGenerateSequenceUpToLengthLimit() {
        // given
        final String sequence = "abcdefg";
        final int limit = 5;
        final MarkovChain<String> chain = MarkovChainBuilder.newInstance(3, "z").populate(explode(sequence)).build();
        final BoundedChainIterator<String> iterator = new BoundedChainIterator<>(chain, limit);

        // when
        final String result = Joiner.on("").join(iterator);

        // then
        assertEquals(sequence.substring(0, limit), result);
    }

    private static List<String> explode(String s) {
        final List<String> result = new ArrayList<>();

        for (char c : s.toCharArray()) {
            result.add(new String(new char[] {c}));
        }

        return result;
    }
}

