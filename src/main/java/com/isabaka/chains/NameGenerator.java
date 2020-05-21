package com.isabaka.chains;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Multiset;
import com.isabaka.chains.markov.MarkovChain;
import com.isabaka.chains.markov.training.Probabilities;
import com.isabaka.chains.markov.training.StartingKeys;
import com.isabaka.chains.markov.training.Training;
import com.isabaka.chains.util.ReadAllLines;
import com.isabaka.chains.util.ResourceByName;

public class NameGenerator {

    public static final String TERMINAL_ELEMENT = System.lineSeparator();

    public static void main(String[] args) {

        new NameGenerator(Stream.of("/seed/names/russia/names-slavic-m.txt").map(new ResourceByName()))
                .generateNames(100000, 50)
                .forEach(System.out::println);

    }

    private final Set<String> trainingSet;
    private final MarkovChain<String> markovChain;

    public NameGenerator(Stream<File> inputData) {
        this.trainingSet = inputData.flatMap(new ReadAllLines()).collect(Collectors.toSet());

        final Training<String> training = new Training<>(3, StartingKeys.firstKeyOfCorpus());

        trainingSet.stream().map(NameGenerator::lineToCharacters).forEach(training::acceptCorpus);

        this.markovChain = new MarkovChain<>(training);
    }

    public List<String> generateNames(int cycles, int topElementsCount) {
        final Probabilities<String> generatedNames = new Probabilities<>();

        for (int i = 0; i < cycles; i++) {
            final String generatedName = markovChain.stream(markovChain.randomStartingKey(), TERMINAL_ELEMENT)
                    .takeWhile(obj -> !TERMINAL_ELEMENT.equals(obj))
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            if (!trainingSet.contains(generatedName)) {
                generatedNames.add(generatedName);
            }
        }

        return generatedNames.getElements().entrySet().stream()
                .sorted(Comparator.comparing(Multiset.Entry::getCount, Comparator.reverseOrder()))
                .limit(topElementsCount)
                .map(Multiset.Entry::getElement)
                .collect(Collectors.toList());
    }

    private static List<String> lineToCharacters(String line) {
        final ArrayList<String> characters = line.codePoints()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));

        characters.add(TERMINAL_ELEMENT);

        return characters;
    }

}
