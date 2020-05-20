package com.isabaka.chains.markov;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("SortedMapWeightedRandom")
public class SortedMapWeightedRandomTest {

    @TestFactory
    @DisplayName("should be created")
    Collection<DynamicTest> shouldBeCreated() {
        return ImmutableList.of(
                dynamicTest("with builder", () -> assertCorrectValues(SortedMapWeightedRandom.<Character>builder()
                        .add(5, 'a')
                        .add(3, 'b')
                        .add(2, 'c')
                        .build())),
                dynamicTest("from multiset",
                        () -> assertCorrectValues(SortedMapWeightedRandom.fromMultiset(ImmutableMultiset.<Character>builder()
                                .add('a', 'a', 'a', 'a', 'a')
                                .add('b', 'b', 'b')
                                .add('c', 'c')
                                .build())))
        );
    }

    @Test
    @DisplayName("should choose value")
    void shouldChooseValue() {
        // given
        final WeightedRandom<Character> weightedRandom = SortedMapWeightedRandom.<Character>builder()
                .add(5, 'a')
                .add(3, 'b')
                .add(2, 'c')
                .build();

        assertAll(
                () -> assertThat(weightedRandom.choose(0d)).isEqualTo('a'),
                () -> assertThat(weightedRandom.choose(0.5d)).isEqualTo('a'),
                () -> assertThat(weightedRandom.choose(0.51d)).isEqualTo('b'),
                () -> assertThat(weightedRandom.choose(0.8d)).isEqualTo('b'),
                () -> assertThat(weightedRandom.choose(0.81d)).isEqualTo('c'),
                () -> assertThat(weightedRandom.choose(0.99d)).isEqualTo('c')
        );

    }

    private static void assertCorrectValues(WeightedRandom<Character> weightedRandom) {
        assertAll(
                () -> assertThat(weightedRandom.values().get(5d)).as("should contain correct entry for 'a'").isEqualTo('a'),
                () -> assertThat(weightedRandom.values().get(8d)).as("should contain correct entry for 'b'").isEqualTo('b'),
                () -> assertThat(weightedRandom.values().get(10d)).as("should contain correct entry for 'c'").isEqualTo('c')
        );
    }

}