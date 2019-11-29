package main;

import main.MainUtils.ProgressBar;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class Main {

    // PARAMETERS

    // Execute first part -> true, second part -> false
    private static final boolean FIRST_PART = false;

    // For second part: Execute it a lot of times to measure performance. Otherwise execute only once for P.o.C.
    private static final boolean PERFORMANCE_MEASUREMENT = true;

    // The rack
    private static Rack rack = new Rack();

    public static void main (String[] args) throws Exception {
        if (FIRST_PART) {
            sortingBallsPart();
        } else {
            sortingCharactersPart();
        }
    }

    /**
     * FIRST PART
     * Reads one number from standard input, adds it to the rack and prints the current rack state.
     * If something other than a number is given, then an error message is shown and prompts for a new number.
     */
    private static void sortingBallsPart () {
        MainUtils.readFromInputAndApplyFunction("Please enter a number. Enter an empty line to break.", (line) -> {
            try {
                rack.add(Integer.parseInt(line));
                System.out.println(rack.order());
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a number.");
            }
        });
    }

    /**
     * SECOND PART
     * Reads the one line sentence from the resource file. Then if we are measuring the performance, we run the applying
     * function a lot of times and printing the average performance. Otherwise we run it only once and print the final
     * state.
     * Then we have the two following scenarios for the applying function:
     * 1. If we are measuring the performance (incrementalPrint = false):
     * We add all the letters in the rack while also sorting them. In the end, return the concatenation of the ordered
     * letters.
     * 2. If we are not measuring the performance (incrementalPrint = true):
     * For every letter, we add it in the rack and print the concatenation of the ordered letters in the current state.
     *
     * @throws Exception If the resource file cannot be found
     */
    private static void sortingCharactersPart () throws Exception {
        final String sentence = Optional.ofNullable(MainUtils.readLinesFromResources("sentence.txt"))
            .orElseThrow(() -> new Exception("Could not read" + " resource"))
            .get(0);

        final Function<Boolean, String> sentenceIntoRackFunction = incrementalPrint -> {
            for (int i = 0; i < sentence.length(); i++) {
                char c = sentence.charAt(i);
                if (!Character.isAlphabetic(c)) {
                    continue;
                }
                rack.add(c);
                if (incrementalPrint) {
                    System.out.println(rack.order()
                        .stream()
                        .map(Object::toString)
                        .reduce("", String::concat));
                }
            }
            if (!incrementalPrint) {
                return rack.order()
                    .stream()
                    .map(Object::toString)
                    .reduce("", String::concat);
            }
            return null;
        };

        if (PERFORMANCE_MEASUREMENT) {
            final int limit = 10000;
            final ProgressBar progressBar = new ProgressBar(limit);
            final long[] performances = new long[limit];
            for (int i = 0; i < limit; i++) {
                final long start = System.currentTimeMillis();
                progressBar.step();
                rack = new Rack();
                sentenceIntoRackFunction.apply(!PERFORMANCE_MEASUREMENT);
                performances[i] = System.currentTimeMillis() - start;
            }
            System.out.println("Performance details: With " + rack.getClass()
                .getSimpleName() + " it took an average of " + (Arrays.stream(performances)
                .reduce(Long::sum)
                .getAsLong() / (double) limit) + " milliseconds");
        } else {
            System.out.println(sentenceIntoRackFunction.apply(PERFORMANCE_MEASUREMENT));
        }
    }

}
