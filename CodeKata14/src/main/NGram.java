package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NGram {

    /**
     * Returns the n-grams of size {@code size} in a {@link Map} that maps a {@link List} of {@code size} consecutive
     * words (the n-gram) to a {@link List} of possible successors.
     * <p>
     * Example: text = "I may love n-grams and I may love or hate probabilities and I may love not having a life." size
     * = 3 Resulting map:
     * <pre>
     *     [I, may love] -> [n-grams, or, not]
     *     [love, n-grams, and] -> [and]
     *     [n-grams, and, I] -> [may]
     *     [and, I, may] -> [love, love]
     *     [may, love, or] -> [hate]
     *     ...
     * </pre>
     * Notice that the right-hand side list is not a set and may contain the same word multiple times. This helps with
     * the aspect of probabilities, as when randomly selecting a successor of an n-gram, the events should not be
     * equiprobable.
     *
     * @param text The text to extract the n-grams
     * @param size The size of the n-grams
     * @return The extracted n-grams
     */
    static Map<List<String>, List<String>> ngram (String text, int size) {
        List<String> words = Arrays.asList(text.split("\\s"));
        Map<List<String>, List<String>> ngrams = new HashMap<>();
        for (int i = 0; i < words.size() - size; i++) {
            List<String> key = new ArrayList<>(words.subList(i, i + size));
            String value = words.get(i + size);
            ngrams.computeIfAbsent(key, newList -> new ArrayList<>()).add(value);
        }
        return ngrams;
    }

    static String sentenceGenerator (Map<List<String>, List<String>> ngrams, String start, int limit) {
        final StringBuilder result = new StringBuilder(start);
        final int size = start.split("\\s").length;

        List<String> previousStart = new ArrayList<>();
        while (true) {
            List<String> words = Arrays.asList(result.toString().split("\\s"));
            List<String> startList = words.subList(words.size() - size, words.size());
            List<String> nextOnes = ngrams.get(startList);
            if (nextOnes == null || startList.equals(previousStart) || result.length() > limit) {
                break;
            }
            String nextOne = nextOnes.get((int) Math.floor(Math.random() * nextOnes.size()));
            result.append(" ").append(nextOne);
            previousStart = startList;
        }
        return result.toString();
    }

}
