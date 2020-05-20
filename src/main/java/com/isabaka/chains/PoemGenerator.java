package com.isabaka.chains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.isabaka.chains.markov.Key;
import com.isabaka.chains.markov.UnboundedMarkovChain;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isAlpha;

public class PoemGenerator {

    public static final ImmutableSet<String> PUNCTUATION = ImmutableSet.of(".", ",", ":", ";", "?", "!", "«", "»", "-", "…", "—");

    public static void main(String[] args) {

        final UnboundedMarkovChain<String> markovChain = Stream.of("/pop.txt", "/ryback.txt", "/rusl.txt")
                .map(PoemGenerator.class::getResource)
                .map(PoemGenerator::readAllLines)
                .flatMap(Collection::stream)
                .flatMap(line -> Arrays.stream(ArrayUtils.add(StringUtils.splitByCharacterTypeCamelCase(line), System.lineSeparator())))
                .filter(element -> element.equals(System.lineSeparator()) || !StringUtils.isBlank(element))
                .collect(UnboundedMarkovChain.collector(2, "." + System.lineSeparator(), PoemGenerator::isStartingKey));

        IntStream.range(0, 25)
                .mapToObj(i -> markovChain.stream(markovChain.randomStartingKey())
                        .limit(35)
                        .reduce("", PoemGenerator::joinElements))
                .forEach(verse -> System.out.printf("%s%n", verse));
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

    private static List<String> readAllLines(URL url) {
        try {
            return Files.readAllLines(Paths.get(url.toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
