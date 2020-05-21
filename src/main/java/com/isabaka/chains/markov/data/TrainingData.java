package com.isabaka.chains.markov.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.isabaka.chains.markov.Key;

public class TrainingData<T> {

    private BiMap<ElementIndex, T> dictionary;

    private BiMap<KeyIndex, Key<T>> keyDictionary;

    private Map<KeyIndex, Probabilities<ElementIndex>> chain;

    private int nextDictionaryIndex = 0;
    private int nextKeyIndex = 0;

    public TrainingData() {
        this.dictionary = HashBiMap.create();
        this.keyDictionary = HashBiMap.create();
        this.chain = new HashMap<>();
    }

    public Key<T> getKeyByIndex(KeyIndex index) {
        return Objects.requireNonNull(keyDictionary.get(index));
    }

    public T getElementByIndex(ElementIndex index) {
        return Objects.requireNonNull(dictionary.get(index));
    }

    public KeyIndex getKeyIndex(Key<T> key) {
        if (keyDictionary.containsValue(key)) {
            return keyDictionary.inverse().get(key);
        } else {
            throw new IllegalStateException("Starting key %s is not in the dictionary");
        }
    }

    public Set<Key<T>> getAllKeys() {
        return chain.keySet().stream().map(this::getKeyByIndex).collect(Collectors.toSet());
    }

    public Probabilities<T> getElementsAfterKey(Key<T> key) {
        final KeyIndex keyIndex = keyDictionary.inverse().get(key);
        return chain.get(keyIndex).map(this::getElementByIndex);
    }

    public void addToChain(Key<T> key, T element) {
        final KeyIndex keyIndex = addKeyToDictionary(key);
        final ElementIndex elementIndex = addElementToDictionary(element);
        chain.computeIfAbsent(keyIndex, ki -> new Probabilities<>()).add(elementIndex);
    }

    private ElementIndex addElementToDictionary(T element) {
        if (dictionary.containsValue(element)) {
            return dictionary.inverse().get(element);

        } else {
            final ElementIndex index = new ElementIndex(nextDictionaryIndex++);
            dictionary.put(index, element);
            return index;
        }
    }

    private KeyIndex addKeyToDictionary(Key<T> key) {
        for (T keyElement : key.values()) {
            addElementToDictionary(keyElement);
        }

        if (keyDictionary.containsValue(key)) {
            return keyDictionary.inverse().get(key);
        } else {
            final KeyIndex index = new KeyIndex(nextKeyIndex++);
            keyDictionary.put(index, key);
            return index;
        }
    }

}
