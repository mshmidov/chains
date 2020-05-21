package com.isabaka.chains.markov.data;

import com.isabaka.chains.markov.ArrayKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainingDataTest {

    @Test
    @DisplayName("should store probabilities")
    void shouldStoreProbabilities() {
        // given
        final TrainingData<String> trainingData = new TrainingData<>();

        final ArrayKey<String> keyA = new ArrayKey<>(new String[] {"A"});
        final ArrayKey<String> keyB = new ArrayKey<>(new String[] {"B"});

        trainingData.addToChain(keyA, "1");
        trainingData.addToChain(keyA, "2");
        trainingData.addToChain(keyA, "2");
        trainingData.addToChain(keyA, "3");
        trainingData.addToChain(keyA, "3");
        trainingData.addToChain(keyA, "3");

        trainingData.addToChain(keyB, "b1");
        trainingData.addToChain(keyB, "b2");
        trainingData.addToChain(keyB, "b2");
        trainingData.addToChain(keyB, "b3");
        trainingData.addToChain(keyB, "b3");
        trainingData.addToChain(keyB, "b3");

        // when
        final Probabilities<String> afterKeyA = trainingData.getElementsAfterKey(keyA);
        final Probabilities<String> afterKeyB = trainingData.getElementsAfterKey(keyB);

        //then
        assertThat(afterKeyA.getElements()).containsExactlyInAnyOrder("1", "2", "2", "3", "3", "3");
        assertThat(afterKeyB.getElements()).containsExactlyInAnyOrder("b1", "b2", "b2", "b3", "b3", "b3");
    }

}
