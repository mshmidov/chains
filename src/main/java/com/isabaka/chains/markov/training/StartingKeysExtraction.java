package com.isabaka.chains.markov.training;

public interface StartingKeysExtraction<T> {

    StaringKeysTraining<T> forOrder(int order);

}
