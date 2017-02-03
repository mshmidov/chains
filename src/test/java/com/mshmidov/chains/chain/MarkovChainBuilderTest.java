package com.mshmidov.chains.chain;

import com.google.common.collect.ImmutableCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class MarkovChainBuilderTest {

    @Test
    @DisplayName("should build Markov chain")
    void shouldBuildMarkovChain() {
        // given
        final int order = 3;
        final String terminal = " ";
        final String[] data = new String[] {"a", "b", "c", "d", "e", "f", "g", " ", "z", " ", "a", "z", " ", "0", "1", "2", "3", "4", " ", "5", "6", "7"};

        final MarkovChainBuilder<String> builder = new MarkovChainBuilder<>(order, terminal);

        // when
        final MarkovChain<String> chain = builder.populate(data).build();

        // then
        final ImmutableCollection<Key<String>> startingKeys = chain.getStartingKeys().values().values();

        assertThat(startingKeys).containsExactly(key("a", "b", "c"), key("0", "1", "2"), key("5", "6", "7"));

        assertThat(chain.getNextElements(key("a", "b", "c")).get().values().values()).containsExactly("d");
        assertThat(chain.getNextElements(key("b", "c", "d")).get().values().values()).containsExactly("e");
        assertThat(chain.getNextElements(key("c", "d", "e")).get().values().values()).containsExactly("f");
        assertThat(chain.getNextElements(key("d", "e", "f")).get().values().values()).containsExactly("g");
        assertThat(chain.getNextElements(key("e", "f", "g")).get().values().values()).containsExactly(terminal);

        assertThat(chain.getNextElements(key("0", "1", "2")).get().values().values()).containsExactly("3");
        assertThat(chain.getNextElements(key("1", "2", "3")).get().values().values()).containsExactly("4");
        assertThat(chain.getNextElements(key("2", "3", "4")).get().values().values()).containsExactly(terminal);
        assertThat(chain.getNextElements(key("5", "6", "7")).get().values().values()).containsExactly(terminal);

    }

    private static <T> Key<T> key(T... elements) {
        return new ArrayKey<>(elements);
    }
}
