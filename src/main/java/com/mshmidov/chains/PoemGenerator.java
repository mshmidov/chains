package com.mshmidov.chains;

import com.google.common.collect.ImmutableSet;
import com.mshmidov.chains.markov.MarkovChain;
import com.mshmidov.chains.markov.MarkovChainBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

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

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isAlpha;

public class PoemGenerator {

    public static void main(String[] args) {

        final MarkovChainBuilder<String> builder = MarkovChainBuilder.newInstance(2, ".");

        final List<String> elements = Stream.of("/pop.txt", "/ryback.txt", "/rusl.txt")
                .map(fileName -> PoemGenerator.class.getResource(fileName))
                .map(PoemGenerator::readAllLines)
                .flatMap(Collection::stream)
                .flatMap(line -> Arrays.stream(ArrayUtils.add(StringUtils.splitByCharacterTypeCamelCase(line), System.lineSeparator())))
                .filter(element -> element.equals(System.lineSeparator()) || !StringUtils.isBlank(element))
                .collect(toList());

        builder.populate(elements);

        final MarkovChain<String> chain = builder.build();

        IntStream.range(0, 25).forEach(i -> {

            final StringBuilder verse = new StringBuilder();

            final ImmutableSet<String> punctuation = ImmutableSet.of(",", ":", ";", "?", "!");

            String previousElement = "";

            for (String element : chain.asIterable(25L)) {

                if ((isAlpha(previousElement) && !punctuation.contains(element)) || punctuation.contains(previousElement)) {
                    verse.append(" ");
                }

                verse.append(element);

                previousElement = element;
            }

            System.out.print(verse);
            System.out.print(".");

        });
    }


    private static List<String> readAllLines(URL url) {
        try {
            return Files.readAllLines(Paths.get(url.toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
