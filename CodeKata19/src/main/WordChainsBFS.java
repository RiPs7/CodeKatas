package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static main.WordChainUtils.levenshteinDistance;
import static main.WordChainUtils.reconstructChain;

class WordChainsBFS implements WordChains {

    private Map<Integer, Set<String>> sameNumberLetterWords;

    WordChainsBFS (final String resource) throws Exception {
        final List<String> words = MainUtils.readLinesFromResources(resource);
        sameNumberLetterWords = Optional.ofNullable(words)
            .orElseThrow(() -> new Exception("Could not read from " + resource))
            .stream()
            .collect(groupingBy(String::length, mapping(String::toLowerCase, toSet())));
    }

    @Override
    public Map<Integer, Set<String>> getSameNumberLetterWords () {
        return sameNumberLetterWords;
    }

    @Override
    public List<String> getChainBetweenWords (String start, String end) throws Exception {
        // Find all the words that have the same number of letters as the starting word.
        final Set<String> wordSublistWithStart = sameNumberLetterWords.entrySet()
            .stream()
            .filter(integerListEntry -> integerListEntry.getValue()
                .contains(start))
            .findFirst()
            .orElseThrow(() -> new Exception("Could not find " + start + " in the wordlist"))
            .getValue();

        // For each word get all the words that are one letter distant (neighbors)
        final Map<String, Set<String>> oneLetterDistantWords = new HashMap<>();
        for (final String word1 : wordSublistWithStart) {
            for (final String word2 : wordSublistWithStart) {
                if (word1.equals(word2)) {
                    continue;
                }
                if (levenshteinDistance(word1, word2) == 1) {
                    oneLetterDistantWords.merge(word1, new HashSet<>() {{
                        add(word2);
                    }}, (existingSet, newSet) -> {
                        existingSet.addAll(newSet);
                        return existingSet;
                    });
                    oneLetterDistantWords.merge(word2, new HashSet<>() {{
                        add(word1);
                    }}, (existingSet, newSet) -> {
                        existingSet.addAll(newSet);
                        return existingSet;
                    });
                }
            }
        }

        // Implementation of balancing BFS to find the shortest path between start and end;

        // The frontier of BFS (this is meant to be a Queue, but because it will be getting sorted, a list will be used)
        final List<String> frontier = new ArrayList<>() {{
            add(start);
        }};
        // Map to keep track the from-to relationship
        final Map<String, String> cameFrom = new HashMap<>();
        // The closed set of BFS
        final Set<String> closedSet = new HashSet<>();

        // Keep track of the previous 'current' to accommodate the 'cameFrom' relationships
        String previous = null;

        // while the frontier is empty...
        while (!frontier.isEmpty()) {
            // remove and get the first element of the frontier
            final String current = frontier.remove(0);
            // if it is equal to the end the finish and reconstruct the chain.
            if (current.equals(end)) {
                // update last cameFrom relationship by using 'previous'
                cameFrom.put(current, previous);
                return reconstructChain(cameFrom, start, end);
            }
            // if it is included in the closed set, move to the next.
            if (closedSet.contains(current)) {
                continue;
            }
            // otherwise, add it to the closed set
            closedSet.add(current);
            // and use 'previous' to establish the cameFrom relationship.
            cameFrom.put(current, previous);
            // get the neighbors (words that can be formed with one letter substitution)
            final Set<String> neighbors = oneLetterDistantWords.get(current);
            // add them to the frontier and define the from-to relationships
            try {
                frontier.addAll(neighbors);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // sort the frontier
            frontier.sort(Comparator.comparingInt(w -> levenshteinDistance(w, end)));
            // update 'previous'
            previous = current;
        }

        throw new Exception("Could not find chain from " + start + " to " + end);
    }

}
