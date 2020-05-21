package com.isabaka.chains;

import java.util.Arrays;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.isabaka.chains.markov.Key;
import com.isabaka.chains.markov.training.StartingKeys;
import com.isabaka.chains.markov.MarkovChain;
import com.isabaka.chains.markov.training.Training;
import com.isabaka.chains.util.ReadAllLines;
import com.isabaka.chains.util.ResourceByName;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isAlpha;

public class PoemGenerator {

    public static final ImmutableSet<String> PUNCTUATION = ImmutableSet.of(".", ",", ":", ";", "?", "!", "«", "»", "-", "…", "—");

    public static void main(String[] args) {

        final Training<String> training = new Training<>(2, StartingKeys.anyKeySatisfying(PoemGenerator::isStartingKey));

        Stream.of("/pop.txt", "/ryback.txt", "/rusl.txt")
                .map(new ResourceByName())
                .flatMap(new ReadAllLines())
                .flatMap(line -> Arrays.stream(ArrayUtils.add(StringUtils.splitByCharacterTypeCamelCase(line), System.lineSeparator())))
                .filter(element -> element.equals(System.lineSeparator()) || !StringUtils.isBlank(element))
                .forEach(training::acceptElement);

        final MarkovChain<String> markovChain = new MarkovChain<>(training, "." + System.lineSeparator());

        for (int i = 0; i < 25; i++) {
            final String verse = markovChain.stream(markovChain.randomStartingKey())
                    .limit(35)
                    .reduce("", PoemGenerator::joinElements);

            System.out.printf("%s%n", verse);
        }
    }

    private static boolean isStartingKey(Key<String> key) {
        final String firstLetter = key.toString().substring(0, 1);
        return StringUtils.isAllUpperCase(firstLetter);
    }

    private static String joinElements(String a, String b) {
        final String lastLetterOfA = StringUtils.substring(a, -1);
        final String firstLetterOfB = b.substring(0, 1);

        if ((isAlpha(lastLetterOfA) && !PUNCTUATION.contains(firstLetterOfB)) || PUNCTUATION.contains(lastLetterOfA)) {
            return a + " " + b;
        } else {
            return a + b;
        }
    }

}
