package com.isabaka.chains.markov.training;

import java.util.Collection;
import java.util.Objects;

import com.isabaka.chains.markov.ArrayKey;
import com.isabaka.chains.markov.Key;
import com.isabaka.chains.markov.data.FinishedTraining;
import com.isabaka.chains.markov.data.Probabilities;
import com.isabaka.chains.markov.data.TrainingData;

import static com.google.common.base.Preconditions.checkArgument;

public class Training<T> {

    private final int order;
    private final DisplacementBuffer<T> buffer;

    private final TrainingData<T> trainingData;
    private final StaringKeysTrainingData<T> staringKeysTraining;

    public Training(int order, StartingKeysExtraction<T> startingKeysExtraction) {
        this.order = order;
        this.buffer = new DisplacementBuffer<>(order);
        this.trainingData = new TrainingData<>();
        this.staringKeysTraining = startingKeysExtraction.forOrder(order);
    }

    public int getOrder() {
        return order;
    }

    public FinishedTraining<T> finishTraining() {
        return new FinishedTraining<>(order, trainingData, staringKeysTraining.getStartingKeys().map(trainingData::getKeyIndex));
    }

    public void acceptElement(T element) {
        addToBuffer(element);
        staringKeysTraining.acceptElement(element);
    }

    public void acceptCorpus(Collection<T> elements) {
        elements.forEach(this::addToBuffer);
        staringKeysTraining.acceptCorpus(elements);
    }

    private void addToBuffer(T element) {
        Objects.requireNonNull(element);

        if (buffer.isFull()) {
            final Key<T> key = new ArrayKey<>(buffer.getData());
            addToChain(key, element);
        }

        buffer.add(element);
    }

    private void addToChain(Key<T> key, T element) {
        checkArgument(key.order() == order);
        trainingData.addToChain(key, element);
    }

}
