package main;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sun.deploy.util.StringUtils;

public class Main {

  private static final boolean DEBUG = false;
  private static final boolean OUTCOME = false;

  public static void main (String[] args) {
    String text = StringUtils.join(
        Optional.ofNullable(MainUtils.readLinesFromResources("sentence.txt"))
            .orElseThrow( () -> new RuntimeException("Could not read file")),
        " ");

    Map<List<String>, List<String>> ngrams = NGram.ngram(text, 2);
    if (DEBUG) {
      for (Map.Entry<List<String>, List<String>> entry : ngrams.entrySet()) {
        if (entry.getValue().size() > 1) {
          System.out.println(entry.getKey() + " -> " + entry.getValue().size());
        }
      }
    }
    String result = NGram.sentenceGenerator(ngrams, "n-gram models", 100);

    System.out.println(result);

    if (OUTCOME) {
        System.out.println(text.contains(result) ? "not so random :(" : "slightly random :)");
    }

  }

}
