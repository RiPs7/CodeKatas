package main;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class WordsFun {

  private static final Map<String, Long> CHAR_MAP = initiateCharacterMapping();

  private static Map<String, Long> initiateCharacterMapping () {
    final List<String> characters =
        IntStream.range(32, 126).mapToObj(i -> Character.toString((char) i)).collect(Collectors.toList());
    final List<Long> eSieve = createEratosthenesSieve(1000);
    return IntStream.of(characters.size()).boxed().collect(toMap(characters::get, eSieve::get));
  }

  private static List<Long> createEratosthenesSieve(int n) {
      boolean primes[] = new boolean[n + 1];
      for (int i = 0; i < n; i++) {
          primes[i] = true;
      }

      int limit = (int) Math.sqrt(n);
      for (int p = 2; p <= limit + 1; p++) {
          if (primes[p]) {
              for (int i = 2 * p; i <= n; i += p) {
                  primes[i] = false;
              }
          }
      }

      return LongStream.of()
  }

  static long calculateWordHash (String word) {

  }

}
