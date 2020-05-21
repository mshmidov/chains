package com.isabaka.chains.markov.training;

import java.util.List;
import java.util.Map;

import com.isabaka.chains.markov.ArrayKey;
import com.isabaka.chains.markov.Key;
import com.isabaka.chains.markov.training.Probabilities;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainingTest {

    @Test
    @DisplayName("should store probabilities")
    void shouldStoreProbabilities() {
        // given
        final Training<String> training = new Training<>(1, StartingKeys.firstKeyOfCorpus());

        training.acceptCorpus(List.of("A", "1"));
        training.acceptCorpus(List.of("A", "2"));
        training.acceptCorpus(List.of("A", "2"));
        training.acceptCorpus(List.of("A", "3"));
        training.acceptCorpus(List.of("A", "3"));
        training.acceptCorpus(List.of("A", "3"));

        training.acceptCorpus(List.of("B", "b1"));
        training.acceptCorpus(List.of("B", "b2"));
        training.acceptCorpus(List.of("B", "b2"));
        training.acceptCorpus(List.of("B", "b3"));
        training.acceptCorpus(List.of("B", "b3"));
        training.acceptCorpus(List.of("B", "b3"));

        // when
        final Map<Key<String>, Probabilities<String>> trainingData = training.getTrainingData();

        //
        final ArrayKey<String> keyA = new ArrayKey<>(new String[] {"A"});
        final ArrayKey<String> keyB = new ArrayKey<>(new String[] {"B"});

        assertThat(trainingData.get(keyA).getElements()).containsExactlyInAnyOrder("1", "2", "2", "3", "3", "3");
        assertThat(trainingData.get(keyB).getElements()).containsExactlyInAnyOrder("b1", "b2", "b2", "b3", "b3", "b3");
    }
}
