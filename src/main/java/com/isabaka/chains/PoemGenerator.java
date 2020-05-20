package com.isabaka.chains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.isabaka.chains.markov.Key;
import com.isabaka.chains.markov.UnboundedMarkovChain;
import com.isabaka.chains.util.ReadAllLines;
import com.isabaka.chains.util.ResourceByName;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isAlpha;

public class PoemGenerator {

    public static final ImmutableSet<String> PUNCTUATION = ImmutableSet.of(".", ",", ":", ";", "?", "!", "«", "»", "-", "…", "—");

    public static void main(String[] args) {

        final UnboundedMarkovChain<String> markovChain = Stream.of("/pop.txt", "/ryback.txt", "/rusl.txt")
                .map(new ResourceByName())
                .flatMap(new ReadAllLines())
                .flatMap(line -> Arrays.stream(ArrayUtils.add(StringUtils.splitByCharacterTypeCamelCase(line), System.lineSeparator())))
                .filter(element -> element.equals(System.lineSeparator()) || !StringUtils.isBlank(element))
                .collect(UnboundedMarkovChain.collector(2, "." + System.lineSeparator(), PoemGenerator::isStartingKey));

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
