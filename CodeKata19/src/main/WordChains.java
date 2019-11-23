package main;

import java.util.List;
import java.util.Map;
import java.util.Set;

interface WordChains {

    default List<String> getShortestChainBetweenWordsOfLength (final int length) throws Exception {
        final Set<String> listOfWordsWithSameNumberOfLetters = getSameNumberLetterWords().get(length);
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
                    final List<String> chain = getChainBetweenWords(start, end);
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

    List<String> getChainBetweenWords (final String start, final String end) throws Exception;

    Map<Integer, Set<String>> getSameNumberLetterWords ();
}
