package com.isabaka.chains;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.isabaka.chains.markov.Key;
import com.isabaka.chains.markov.MarkovChain;
import com.isabaka.chains.markov.training.StartingKeys;
import com.isabaka.chains.markov.training.Training;
import com.isabaka.chains.util.ReadAllLines;
import com.isabaka.chains.util.ResourceByName;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isAlpha;

public class PoemGenerator {

    public static final ImmutableSet<String> SPACE_AFTER = ImmutableSet.of(".", ",", ":", ";", "?", "!", "»", "-", "…", "—");
    public static final ImmutableSet<String> SPACE_BEFORE = ImmutableSet.of("«", "-", "—");
    public static final Sets.SetView<String> PUNCTUATION = Sets.union(SPACE_AFTER, SPACE_BEFORE);

    public static final String TERMINAL_ELEMENT = ". ";

    public static void main(String[] args) {

        final PoemGenerator poemGenerator = new PoemGenerator(Stream.of("/pop.txt", "/ryback.txt", "/rusl.txt")
                .map(new ResourceByName()));

        for (int i = 0; i < 25; i++) {
            System.out.println(poemGenerator.generateText(40));
        }
    }

    private final MarkovChain<String> markovChain;

    public PoemGenerator(Stream<File> inputData) {
        final Training<String> training = new Training<>(2, StartingKeys.anyKeySatisfying(PoemGenerator::isStartingKey));

        inputData.flatMap(new ReadAllLines())
                .flatMap(PoemGenerator::splitLine)
                .filter(element -> element.equals(System.lineSeparator()) || !StringUtils.isBlank(element))
                .forEach(training::acceptElement);

        this.markovChain = new MarkovChain<>(training);
    }

    public String generateText(int words) {
        return markovChain.stream(markovChain.randomStartingKey(), TERMINAL_ELEMENT)
                .limit(words)
                .reduce("", PoemGenerator::joinElements);
    }

    private static boolean isStartingKey(Key<String> key) {
        final String firstLetter = key.toString().substring(0, 1);
        return StringUtils.isAllUpperCase(firstLetter);
    }

    private static Stream<String> splitLine(String line) {
        final Stream<String> elements = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(line))
                .map(String::strip);

        return Stream.concat(elements, Stream.of(System.lineSeparator()));
    }

    private static String joinElements(String a, String b) {
        final String lastLetterOfA = StringUtils.substring(a, -1);
        final String firstLetterOfB = b.substring(0, 1);

        final boolean spaceBetweenWords = isAlpha(lastLetterOfA) && isAlpha(firstLetterOfB);
        final boolean spaceAroundPunctuation = SPACE_AFTER.contains(lastLetterOfA) ^ SPACE_BEFORE.contains(firstLetterOfB);

        if (spaceBetweenWords || spaceAroundPunctuation) {
            return a + " " + b;
        } else {
            return a + b;
        }
    }

}
