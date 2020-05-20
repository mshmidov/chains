package com.isabaka.chains;

import com.google.common.base.Joiner;
import com.isabaka.chains.markov.MarkovChain;
import com.isabaka.chains.markov.MarkovChainBuilder;
import com.isabaka.chains.util.IterableString;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NameGenerator {

    public static void main(String[] args) throws IOException, URISyntaxException {

        final MarkovChainBuilder<Character> builder = MarkovChainBuilder.newInstance(3, Character.MAX_VALUE);

        try (Stream<String> names = Files.lines(Paths.get(NameGenerator.class.getResource("/names.ru").toURI()))) {
            names.map(IterableString::asCharacters).forEach(builder::populate);
        }

        final MarkovChain<Character> chain = builder.build();
        final Joiner letterJoiner = Joiner.on("");

        IntStream.range(0, 25)
                .mapToObj(i -> chain.asIterable(1000L))
                .map(letterJoiner::join)
                .forEach(System.out::println);
    }



}
