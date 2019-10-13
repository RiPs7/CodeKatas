package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    private static final String RESOURCE_FILENAME = "res/word-list.txt";

    private static final int LIMIT_RAND_STRINGS = 1000;

    private static final int RAND_STRING_LENGTH = 5;

    private static final boolean MULTI_HASH_BLOOM_FILTER = false;

    public static void main (String[] args) {
        loadWordListAndCalculateFalsePositives(BloomFilterAlgorithm.SHA256);
        loadWordListAndCalculateFalsePositives(BloomFilterAlgorithm.SHA512);
        loadWordListAndCalculateFalsePositives(BloomFilterAlgorithm.SHA512_EXT);
    }

    private static void loadWordListAndCalculateFalsePositives (final BloomFilterAlgorithm bloomFilterAlgorithm) {
        final long start = System.currentTimeMillis();

        // Part 1
        // Load the word list
        final List<String> wordList = Optional.ofNullable(readWordList())
            .orElseThrow(() -> new IllegalArgumentException("Could not read word list"));
        // Create a new bloom filter and add every word in the word list
        BloomFilter bloomFilter;
        try {
            bloomFilter = MULTI_HASH_BLOOM_FILTER ? new BloomFilterMultiHash(bloomFilterAlgorithm) :
                new BloomFilterSingleHash(bloomFilterAlgorithm);
        } catch (IllegalArgumentException e) {
            return;
        }
        wordList.forEach(bloomFilter::addElement);

        // Part 2
        int falsePositives = 0;
        // Generate some random 5-letter words
        for (int i = 0; i < LIMIT_RAND_STRINGS; i++) {
            final String randomString = generateRandomString();
            final boolean existsInBloom = bloomFilter.containsElement(randomString);
            final boolean existsInWordList = wordList.contains(randomString);
            // If it exists in bloom filter and not in the word list then increase the false positives
            if (existsInBloom && !existsInWordList) {
                falsePositives++;
            }
        }
        // Print the error rate
        System.out.println("False positive rate: " + (100.0 * falsePositives / LIMIT_RAND_STRINGS) + "% with " +
            bloomFilterAlgorithm.toString());
        // Print performance time
        System.out.println("It took: " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("----------------------------------------");
    }

    private static List<String> readWordList () {
        // Get the resource as an input stream.
        final InputStream inputStream = ClassLoader.getSystemClassLoader()
            .getResourceAsStream(RESOURCE_FILENAME);
        if (inputStream == null) {
            return null;
        }
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final BufferedReader reader = new BufferedReader(inputStreamReader);

        // Read all the lines
        return reader.lines()
            .collect(Collectors.toList());
    }

    private static String generateRandomString () {
        final int bottomCharLimit = 97; // letter 'a'
        final int topCharLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(RAND_STRING_LENGTH);
        for (int i = 0; i < RAND_STRING_LENGTH; i++) {
            final int randomLimitedInt =
                bottomCharLimit + (int) (random.nextFloat() * (topCharLimit - bottomCharLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

}
