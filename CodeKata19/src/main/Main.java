package main;

public class Main {

    private static final String RESOURCE = "wordlist.txt";

    public static void main (String[] args) {

        MainUtils.readFromInputAndApplyFunction("Please enter 2 words:", (line) -> {
            final String[] words = line.split("[\\s\\t-.,]");

            // BFS is guaranteed to find the shortest chain.
            System.out.println("It took " + MainUtils.timeIt(() -> {
                try {
                    System.out.println(new WordChainsBFS(RESOURCE).getShortestChainBetweenWords(words[0], words[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1) + "ms with BFS\n");

            // An attempt with an enhanced BFS.
            System.out.println("It took " + MainUtils.timeIt(() -> {
                try {
                    System.out.println(
                        new WordChainsEnhancedBFS(RESOURCE).getShortestChainBetweenWords(words[0], words[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1) + "ms with Enhanced BFS\n");

            // A Star is meant to be faster.
            System.out.println("It took " + MainUtils.timeIt(() -> {
                try {
                    System.out.println(new WordChainsAStar(RESOURCE).getShortestChainBetweenWords(words[0], words[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1) + "ms with A Star\n");
        });

    }

}
