package main;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongConsumer;

public class Main {

    public static void main (String[] args) throws Exception {
        final long start = System.currentTimeMillis();
        AtomicLong time = new AtomicLong();

        LongConsumer calculateTime = endTime -> {
            time.set(endTime - start);
        };

        Optional.ofNullable(
            WordsFun.findPermutationsInWordList(MainUtils.readLinesFromResources("word-list.txt"), calculateTime))
            .orElseThrow(() -> new Exception("Could not read word list"))
            .forEach(System.out::println);

        System.out.println(
            "It took: " + (time.get() / 1000.0) + " seconds" + "\n");

        MainUtils.readFromInputAndApplyFunction(input -> {
            System.out.println(WordsFun.findPermutationsOfWord(input));
        });
    }

}
