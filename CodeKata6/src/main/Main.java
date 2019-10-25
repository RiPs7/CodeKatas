package main;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

public class Main {

  public static void main (String[] args) {
    final List<String> words = MainUtils.readLinesFromResources("word-list.txt");
    if (words == null) {
      System.exit(-1);
    }

    final long start = System.currentTimeMillis();

    final Map<Long, String> permutations = words.stream()
        .limit(100000)
        .map(WordsFun::getWordHash)
        .collect(toList());

    System.out.println("Found permutations: " + permutations.toString());

    System.out.println("It took: " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds");
  }

}
