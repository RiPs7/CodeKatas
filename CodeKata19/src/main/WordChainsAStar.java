package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static main.WordChainUtils.levenshteinDistance;

class WordChainsAStar implements WordChains {

    private Map<Integer, Set<String>> sameNumberLetterWords;

    WordChainsAStar (final String resource) throws Exception {
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

        // Implementation of A Star to find the shortest path between start and end;

        // Define the functions
        // g(node) = actual cost from start to node = levenshtein(start, node)
        final Function<String, Integer> g = (node) -> levenshteinDistance(start, node);
        // h(node) = heuristic cost from node to end = levenshtein(node, end)
        final Function<String, Integer> h = (node) -> levenshteinDistance(node, end);
        // f(node) = g(node) + h(node)
        final Function<String, Integer> f = (node) -> g.apply(node) + h.apply(node);

        // The open list that maps a word to its f value
        final TreeMap<String, Integer> openList = new TreeMap<>() {{ put(start, 0); }};

        // The closed list
        final TreeMap<String, Integer> closedList = new TreeMap<>();

        // while open list is not empty...
        while (!openList.isEmpty()) {
            // get the entry with the least f from the open list
            final Map.Entry<String, Integer> current = openList.firstEntry();
            // remove it from it
            openList.remove(current.getKey());
            // get all its neighbors
            final Set<String> neighbors = oneLetterDistantWords.get(current.getKey());

            // for each neighbor...
            for (final String neighbor : neighbors) {
                // if it is the end then stop
                if (end.equals(neighbor)) {
                    int a = 0;
                    //return reconstructChain(cameFrom, start, end);
                }
                // calculate neighbor's f value
                final int neighborF = f.apply(neighbor);
                if (openList.containsKey(neighbor) && openList.get(neighbor) < neighborF) {
                    continue;
                }
                if (closedList.containsKey(neighbor) && closedList.get(neighbor) < neighborF) {
                    continue;
                }
                openList.put(neighbor, neighborF);
            }
            closedList.put(current.getKey(), f.apply(current.getKey()));
        }

        throw new Exception("Could not find chain from " + start + " to " + end);
    }

}
