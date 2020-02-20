package main;

public class Main {

    private static final boolean INTERACTIVE = false;

    private static final String RESOURCE = "wordlist.txt";

    private static final GraphAlgorithm ALGORITHM = GraphAlgorithm.A_STAR;

    public static void main (String[] args) throws Exception {

        final WordChains wordChains;

        switch (ALGORITHM) {
            case BFS:
                wordChains = new WordChainsBFS(RESOURCE);
                break;
            case A_STAR:
                wordChains = new WordChainsAStar(RESOURCE);
                break;
            default:
                throw new Exception("Unrecognized algorithm " + ALGORITHM);
        }

        if (INTERACTIVE) {
            MainUtils.readFromInputAndApplyFunction("Please enter 2 words:", (line) -> {
                final String[] words = line.split("[\\s\\t-.,]");
                try {
                    final long start = System.currentTimeMillis();
                    System.out.println(wordChains.getShortestChainBetweenWords(words[0], words[1]));
                    System.out.println("It took " + (System.currentTimeMillis() - start) + " ms");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            System.out.println(wordChains.getShortestChainBetweenWordsOfLength(4));
        }

    }

}
