package com.mshmidov.chains;

import com.google.common.collect.ImmutableList;
import com.google.common.truth.Truth;
import com.mshmidov.chains.chain.Key;
import com.mshmidov.chains.chain.MarkovChain;
import com.mshmidov.chains.chain.MarkovChainBuilder;
import com.mshmidov.chains.random.WeightedRandom;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkovChainTest {

    @Test
    public void shouldPopulateChain() {

        // given
        final String sequence = "abcdefg";

        // when
        final MarkovChain<Character> chain = new MarkovChainBuilder<>(3, Character.MIN_VALUE).populate(ArrayUtils.toObject(sequence.toCharArray())).build();

        // then
        final WeightedRandom<Key<Character>> startingKeys = chain.getStartingKeys();

        assertEquals(startingKeys.values().size(), 1);
        Key<Character> key = startingKeys.choose(1);
        assertEquals(key.values(), ImmutableList.of('a', 'b', 'c'));

        Optional<WeightedRandom<Character>> next = chain.getNextElements(key);

        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).hasSize(1);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).contains('d');

        key = key.append('d');
        next = chain.getNextElements(key);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).hasSize(1);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).contains('e');

        key = key.append('e');
        next = chain.getNextElements(key);
        assertEquals(next.orElseThrow(IllegalStateException::new).values().values().size(), 1);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).hasSize(1);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).contains('f');

        key = key.append('f');
        next = chain.getNextElements(key);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).hasSize(1);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).contains('g');

        key = key.append('g');
        next = chain.getNextElements(key);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).hasSize(1);
        Truth.assertThat(next.orElseThrow(IllegalStateException::new).values().values()).contains(chain.terminalSymbol());
    }
}
