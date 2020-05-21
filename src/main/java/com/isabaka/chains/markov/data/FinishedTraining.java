package com.isabaka.chains.markov.data;

public class FinishedTraining<T> {

    private int order;

    private TrainingData<T> trainingData;

    private Probabilities<KeyIndex> startingKeys;

    public FinishedTraining() {
        trainingData = new TrainingData<>();
        startingKeys = new Probabilities<>();
    }

    public FinishedTraining(int order, TrainingData<T> trainingData, Probabilities<KeyIndex> startingKeys) {
        this.order = order;
        this.trainingData = trainingData;
        this.startingKeys = startingKeys;
    }

    public int getOrder() {
        return order;
    }

    public TrainingData<T> getTrainingData() {
        return trainingData;
    }

    public Probabilities<KeyIndex> getStartingKeys() {
        return startingKeys;
    }

}
