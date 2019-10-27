package main;

import java.util.Optional;
import java.util.function.Supplier;

public class Main {

    public static void main (String[] args) throws Exception {
        final long start = System.currentTimeMillis();

        Supplier<Void> calculateTime = () -> {
            System.out.println(
                "It took: " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds" + "\n\nPermutations found:");
            return null;
        };

        Optional.ofNullable(
            WordsFun.findPermutationsInWordList(MainUtils.readLinesFromResources("word-list.txt"), calculateTime))
            .orElseThrow(() -> new Exception("Could not read word list"))
            .forEach(System.out::println);
    }

}
