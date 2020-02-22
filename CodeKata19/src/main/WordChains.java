package main;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

abstract class WordChains {

    Map<Integer, Set<String>> sameNumberLetterWords;

    abstract List<String> algorithm (final String start, final String end)
        throws Exception;

    List<String> getShortestChainBetweenWords (final String start, final String end) throws Exception {
        return algorithm(start, end);
    }

    Set<String> getWordsWithOneLetterDistance (final String word) {
        return sameNumberLetterWords.get(word.length())
            .stream()
            .filter(w -> WordChainUtils.levenshteinDistance(w, word) == 1)
            .collect(Collectors.toSet());
    }
}
