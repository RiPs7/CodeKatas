package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static main.WordChainUtils.levenshteinDistance;
import static main.WordChainUtils.reconstructChain;

class WordChainsAStar extends WordChains {

    WordChainsAStar (final String resource) throws Exception {
        sameNumberLetterWords = Optional.ofNullable(MainUtils.readLinesFromResources(resource))
            .orElseThrow(() -> new Exception("Could not read from " + resource))
            .stream()
            .collect(groupingBy(String::length, mapping(String::toLowerCase, toSet())));
    }

    @Override
    List<String> algorithm (final String start, final String end)
        throws Exception {
        // Define the heuristic functions
        // g(node) = actual cost from start to node = levenshtein(start, node)
        final Function<String, Integer> g = (node) -> levenshteinDistance(start, node);
        // h(node) = heuristic cost from node to end = levenshtein(node, end)
        final Function<String, Integer> h = (node) -> levenshteinDistance(node, end);
        // f(node) = g(node) + h(node)
        final Function<String, Integer> f = (node) -> g.apply(node) + h.apply(node);

        // Map to keep track the from-to relationship
        final Map<String, String> cameFrom = new HashMap<>() {{
            put(start, null);
        }};
        // The open list that maps a word to its f value
        final TreeSet<WordWithHeuristic> openList = new TreeSet<>() {{
            add(new WordWithHeuristic(start, 0));
        }};
        // The closed list
        final TreeSet<WordWithHeuristic> closedList = new TreeSet<>();

        // while open list is not empty...
        while (!openList.isEmpty()) {
            // get the entry with the least f from the open list
            final WordWithHeuristic current = openList.first();
            // remove it from it
            openList.remove(current);
            // get all its neighbors
            final Set<String> neighbors = getWordsWithOneLetterDistance(current.word);

            // for each neighbor...
            for (final String neighbor : neighbors) {
                // calculate neighbor's f value
                final int neighborF = f.apply(neighbor);

                final WordWithHeuristic neighborWordWithHeuristic = new WordWithHeuristic(neighbor, neighborF);

                if (openList.contains(neighborWordWithHeuristic)) {
                    continue;
                }
                if (closedList.contains(neighborWordWithHeuristic)) {
                    continue;
                }
                // add it to the open list and define the from-to relationship
                openList.add(neighborWordWithHeuristic);
                if (!neighbor.equals(start) && !cameFrom.containsKey(neighbor)) {
                    cameFrom.put(neighbor, current.word);
                }
                // if it is the end then stop
                if (end.equals(neighbor)) {
                    return reconstructChain(cameFrom, start, end);
                }
            }
            closedList.add(current);
        }

        throw new Exception("Could not find chain from " + start + " to " + end);
    }

    private static final class WordWithHeuristic implements Comparable<WordWithHeuristic> {
        private String word;
        private int heuristic;

        @Override
        public int compareTo (final WordWithHeuristic o) {
            final int heuristicDifference = this.heuristic - o.heuristic;
            return heuristicDifference == 0 ? this.word.compareTo(o.word) : heuristicDifference;
        }

        WordWithHeuristic (final String word, final int heuristic) {
            this.word = word;
            this.heuristic = heuristic;
        }

        @Override
        public boolean equals (final Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof WordWithHeuristic)) {
                return false;
            }
            WordWithHeuristic other = (WordWithHeuristic) obj;
            return this.word.equals(other.word) && this.heuristic == other.heuristic;
        }
    }

}
