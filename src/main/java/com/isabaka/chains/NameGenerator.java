package com.isabaka.chains;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.isabaka.chains.markov.training.StartingKeys;
import com.isabaka.chains.markov.training.Training;
import com.isabaka.chains.markov.MarkovChain;
import com.isabaka.chains.util.ReadAllLines;
import com.isabaka.chains.util.ResourceByName;

public class NameGenerator {

    public static final String TERMINAL_ELEMENT = System.lineSeparator();

    public static void main(String[] args) {

        final Training<String> training = new Training<>(3, StartingKeys.firstKeyOfCorpus());

        Stream.of("/names.ru")
                .map(new ResourceByName())
                .flatMap(new ReadAllLines())
                .map(NameGenerator::lineToCharacters)
                .forEach(training::acceptCorpus);

        final MarkovChain<String> markovChain = new MarkovChain<>(training, TERMINAL_ELEMENT);

        for (int i = 0; i < 25; i++) {
            final String name = markovChain.stream(markovChain.randomStartingKey())
                    .takeWhile(obj -> !TERMINAL_ELEMENT.equals(obj))
                    .limit(25)
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            System.out.println(name);
        }

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
