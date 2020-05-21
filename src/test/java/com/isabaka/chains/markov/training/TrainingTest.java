package com.isabaka.chains.markov.training;

import java.util.List;
import java.util.Map;

import com.isabaka.chains.markov.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainingTest {

    @Test
    @DisplayName("should train on data")
    void shouldTrainOnData() {
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
        final Key<String> keyA = new Key<>("A");
        final Key<String> keyB = new Key<>("B");

        assertThat(trainingData.get(keyA).getElements()).containsExactlyInAnyOrder("1", "2", "2", "3", "3", "3");
        assertThat(trainingData.get(keyB).getElements()).containsExactlyInAnyOrder("b1", "b2", "b2", "b3", "b3", "b3");

        assertThat(training.getStaringKeys().getElements()).containsExactlyInAnyOrder(
                keyA, keyA, keyA, keyA, keyA, keyA,
                keyB, keyB, keyB, keyB, keyB, keyB);
    }
}
