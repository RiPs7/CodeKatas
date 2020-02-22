package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class WordChainUtils {

    private static final int INS_DEL_COST = 2;
    private static final int SUBSTITUTION_COST = 1;

    static List<String> reconstructChain (Map<String, String> cameFrom, String start, String end) {
        List<String> invertedPath = new ArrayList<>() {{
            add(end);
        }};
        String current = end;
        try {
            while (!cameFrom.get(current)
                .equals(start)) {
                current = cameFrom.get(current);
                invertedPath.add(current);
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        invertedPath.add(start);
        Collections.reverse(invertedPath);
        return invertedPath;
    }

    static int levenshteinDistance (final String word1, final String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j * INS_DEL_COST;
                } else if (j == 0) {
                    dp[i][j] = i * INS_DEL_COST;
                } else {
                    dp[i][j] = Math.min(
                        dp[i - 1][j - 1] + (word1.charAt(i - 1) == word2.charAt(j - 1) ? 0 : SUBSTITUTION_COST),
                        Math.min(dp[i - 1][j] + INS_DEL_COST, dp[i][j - 1] + INS_DEL_COST));
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }

}
