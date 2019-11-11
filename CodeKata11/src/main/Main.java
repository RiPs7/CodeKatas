package main;

import main.rack.Rack;
import main.rack.impl.BinaryTreeRack;
import main.rack.impl.RedBlackTreeRack;

import java.util.Optional;
import java.util.function.Function;

public class Main {

    // Parameters
    private static final boolean EXECUTE_FIRST_PART = false;
    private static final boolean USE_RED_BLACK_LOGIC = true;
    private static final boolean PERFORMANCE_MEASUREMENT = false;

    private static Rack rack = USE_RED_BLACK_LOGIC ? new RedBlackTreeRack() : new BinaryTreeRack();

    public static void main (String[] args) throws Exception {
        if (EXECUTE_FIRST_PART) {
            sortingBallsPart();
        } else {
            sortingCharactersPart();
        }
    }

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

    private static void sortingCharactersPart () throws Exception {
        final String sentence = Optional.ofNullable(MainUtils.readLinesFromResources("sentence.txt"))
            .orElseThrow(() -> new Exception("Could not read" + " resource"))
            .get(0);

        final Function<Boolean, String> sentenceIntoRack = incrementalPrint -> {
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
            final long start = System.currentTimeMillis();
            final long limit = 100000;
            for (int i = 0; i < limit; i++) {
                rack = USE_RED_BLACK_LOGIC ? new RedBlackTreeRack() : new BinaryTreeRack();
                sentenceIntoRack.apply(!PERFORMANCE_MEASUREMENT);
            }
            System.out.println("Performance details: With " + rack.getClass().getSimpleName() + " it took an average "
                + "of " + ((System.currentTimeMillis() - start) / (double) limit) + " milliseconds");
        } else {
            System.out.println(sentenceIntoRack.apply(PERFORMANCE_MEASUREMENT));
        }
    }

}
