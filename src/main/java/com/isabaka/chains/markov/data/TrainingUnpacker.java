package com.isabaka.chains.markov.data;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.isabaka.chains.markov.Key;
import org.apache.commons.math3.distribution.EnumeratedDistribution;

public class TrainingUnpacker<T> {

    private final FinishedTraining<T> finishedTraining;

    public TrainingUnpacker(FinishedTraining<T> finishedTraining) {
        this.finishedTraining = finishedTraining;
    }

    public int getOrder() {
        return finishedTraining.getOrder();
    }

    public Map<Key<T>, EnumeratedDistribution<T>> getChain() {
        final TrainingData<T> trainingData = finishedTraining.getTrainingData();

        return trainingData.getAllKeys()
                .stream()
                .collect(Collectors.toMap(Function.identity(),
                        key -> trainingData.getElementsAfterKey(key).toEnumeratedDistribution()));
    }

    public EnumeratedDistribution<Key<T>> getStartingKeys() {
        return finishedTraining.getStartingKeys()
                .map(keyIndex -> finishedTraining.getTrainingData().getKeyByIndex(keyIndex))
                .toEnumeratedDistribution();
    }

}
