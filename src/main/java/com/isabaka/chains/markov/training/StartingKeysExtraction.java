package com.isabaka.chains.markov.training;

public interface StartingKeysExtraction<T> {

    StaringKeysTrainingData<T> forOrder(int order);

}
