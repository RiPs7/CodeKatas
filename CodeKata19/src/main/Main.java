package main;

public class Main {

    private static final boolean INTERACTIVE = true;

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
            MainUtils.readFromInputAndApplyFunction("Please enter 2 words separated by space", (line) -> {
                final String[] words = line.split(" ");
                try {
                    wordChains.getChainBetweenWords(words[0], words[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            wordChains.getShortestChainBetweenWordsOfLength(4);
        }

    }

}
