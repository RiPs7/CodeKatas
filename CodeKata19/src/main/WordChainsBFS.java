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

class WordChainsBFS extends WordChains {

    WordChainsBFS (final String resource) throws Exception {
        sameNumberLetterWords = Optional.ofNullable(MainUtils.readLinesFromResources(resource))
            .orElseThrow(() -> new Exception("Could not read from " + resource))
            .stream()
            .collect(groupingBy(String::length, mapping(String::toLowerCase, toSet())));
    }

    @Override
    List<String> algorithm (final String start, final String end) throws Exception {
        // Implementation of balancing BFS to find the shortest path between start and end;

        // The frontier of BFS (this is meant to be a Queue, but because it will be getting sorted, a list will be used)
        final List<String> frontier = new ArrayList<>() {{
            add(start);
        }};
        // Map to keep track the from-to relationship
        final Map<String, String> cameFrom = new HashMap<>() {{
            put(start, null);
        }};
        // The closed set of BFS
        final Set<String> closedSet = new HashSet<>();

        // while the frontier is empty...
        while (!frontier.isEmpty()) {
            // remove and get the first element of the frontier
            final String current = frontier.remove(0);
            // if it is equal to the end the finish and reconstruct the chain.
            if (current.equals(end)) {
                return reconstructChain(cameFrom, start, end);
            }
            // if it is included in the closed set, move to the next.
            if (closedSet.contains(current)) {
                continue;
            }
            // get the neighbors (words that can be formed with one letter substitution)
            final Set<String> neighbors = getWordsWithOneLetterDistance(current);
            // add them to the frontier and define the from-to relationships (omit when neighbor is the starting word)
            try {
                frontier.addAll(neighbors);
                neighbors.forEach(neighbor -> {
                    if (neighbor.equals(start) || cameFrom.containsKey(neighbor)) {
                        return;
                    }
                    cameFrom.put(neighbor, current);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            // sort the frontier
            frontier.sort(Comparator.comparingInt(w -> levenshteinDistance(w, end)));
            // otherwise, add it to the closed set
            closedSet.add(current);
        }

        throw new Exception("Could not find chain from " + start + " to " + end);
    }

}
