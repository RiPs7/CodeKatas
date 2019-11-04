package main;

import java.util.Optional;

public class Main {

    private static final int PART_TO_EXECUTE = 2;

    public static void main (String[] args) throws Exception {
        if (PART_TO_EXECUTE == 1) {
            sortingBallsPart();
        } else if (PART_TO_EXECUTE == 2) {
            sortingCharactersPart();
        }
    }

    private static void sortingBallsPart () {
        final Rack rack = new Rack();
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
        final Rack rack = new Rack();
        final String sentence = Optional.ofNullable(MainUtils.readLinesFromResources("sentence.txt"))
            .orElseThrow(() -> new Exception("Could not read" + " resource"))
            .get(0);
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (!Character.isAlphabetic(c)) {
                continue;
            }
            rack.add(c);
            System.out.println(rack.order()
                .stream()
                .map(Object::toString)
                .reduce("", String::concat));
        }
    }

}
