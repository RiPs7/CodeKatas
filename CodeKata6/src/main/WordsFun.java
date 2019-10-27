package main;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class WordsFun {

    private static final String ALPHABET = "etaoinshrdlcumwfgypbvkjxqzETAOINSHRDLCUMWFGYPBVKJXQZ'";

    private static final int E_SIEVE_LIMIT = 1000;

    private static final Map<String, Integer> CHAR_MAP = initiateCharacterMapping();

    private static Map<String, Integer> initiateCharacterMapping () {
        final List<String> characters = IntStream.range(0, ALPHABET.length())
            .mapToObj(i -> ALPHABET.substring(i, i + 1))
            .collect(toList());
        final List<Integer> eSieve = createEratosthenesSieve();
        return IntStream.range(0, characters.size())
            .boxed()
            .collect(toMap(characters::get, eSieve::get));
    }

    private static List<Integer> createEratosthenesSieve () {
        final boolean[] composites = new boolean[E_SIEVE_LIMIT + 1];
        int limit = (int) Math.sqrt(E_SIEVE_LIMIT);
        for (int p = 2; p <= limit + 1; p++) {
            if (!composites[p]) {
                for (int i = 2 * p; i <= E_SIEVE_LIMIT; i += p) {
                    composites[i] = true;
                }
            }
        }
        return IntStream.range(2, composites.length)
            .boxed()
            .filter(i -> !composites[i])
            .collect(toList());
    }

    private static Integer calculateWordHash (String word) {
        return IntStream.range(0, word.length())
            .boxed()
            .map(i -> CHAR_MAP.getOrDefault(word.substring(i, i + 1), 0))
            .reduce(1, (accumulator, current) -> accumulator * current);
    }

    /**
     * Based on the observation from relevant research that anagrams of words can be identified through the Fundamental
     * Theorem of Arithmetic: Every positive integer can be written in a unique way as a product of a finite set of
     * primes. So, by mapping all the possible character of the alphabet to a prime number, we can assign to each
     * word the product of the primes corresponding to its letters. This way only the words that consist of the same
     * letters will have the same product assigned to them.
     * A potential improvement can be achieved by sorting the alphabet in a way that the most frequent letters are
     * mapped to smaller primes.
     * If an unrecognized character is found in the word, the whole word will be assigned a product of 0. In the end,
     * all the words with a product of 0 will be removed and not marked as permutations.
     */
    static List<List<String>> findPermutationsInWordList (final List<String> wordList, final Supplier callback) {
        if (wordList == null) {
            return null;
        }
        final Map<String, Integer> wordHashes = wordList.stream()
            .collect(toMap(identity(), WordsFun::calculateWordHash));

        final Map<Integer, List<String>> groupedPermutations = wordHashes.entrySet()
            .stream()
            .collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, toList())));
        groupedPermutations.remove(0);

        final List<List<String>> permutations = groupedPermutations.values()
            .stream()
            .filter(list -> list.size() > 1)
            .collect(toList());

        callback.get();

        return permutations;
    }

}
