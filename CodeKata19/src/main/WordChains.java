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

    List<String> getShortestChainBetweenWordsOfLength (final int length) {
        final Set<String> listOfWordsWithSameNumberOfLetters = sameNumberLetterWords.get(length);
        final int n = listOfWordsWithSameNumberOfLetters.size();
        final MainUtils.ProgressBar progressBar = new MainUtils.ProgressBar((n * n + 1) / 2);
        List<String> shortestChain = null;
        for (final String start : listOfWordsWithSameNumberOfLetters) {
            for (final String end : listOfWordsWithSameNumberOfLetters) {
                if (start.equals(end)) {
                    continue;
                }
                progressBar.step();
                try {
                    final List<String> chain = getShortestChainBetweenWords(start, end);
                    if (shortestChain == null || chain.size() < shortestChain.size()) {
                        shortestChain = chain;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return shortestChain;
    }

    Set<String> getWordsWithOneLetterDistance (final String word) {
        return sameNumberLetterWords.get(word.length())
            .stream()
            .filter(w -> WordChainUtils.levenshteinDistance(w, word) == 1)
            .collect(Collectors.toSet());
    }
}
