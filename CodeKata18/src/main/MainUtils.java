package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class MainUtils {

  static List<String> readLinesFromResources (String filename) {
    // Get the resource as an input stream.
    final InputStream inputStream = ClassLoader.getSystemClassLoader()
        .getResourceAsStream("res/" + filename);
    if (inputStream == null) {
      return null;
    }
    final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    final BufferedReader reader = new BufferedReader(inputStreamReader);

    // Read all the lines
    return reader.lines().filter(line -> !line.startsWith("//")).collect(Collectors.toList());
  }

  static void readFromInputAndApplyFunction (String promptMessage, Consumer<String> function) {
    System.out.println(promptMessage);
    final Scanner scanner = new Scanner(System.in);
    String line;
    while (!(line = scanner.nextLine()).equals("")) {
      function.accept(line);
      System.out.println(promptMessage);
    }
  }

  static double timeIt (Runnable functionality, int times) {
    final long start = System.currentTimeMillis();
    for (int i = 0; i < times; i++) {
      functionality.run();
    }
    final long end = System.currentTimeMillis();
    return (end - start) / (double) times;
  }

  static class ProgressBar {
    // This progress bar has 50 visual steps.
    private long scale;
    private long current;
    private long scaledCurrent;

    ProgressBar (long steps) {
      this.scale = steps / 50;
      this.current = 0;
      this.scaledCurrent = 0;
    }

    void step () {
      this.current++;
      if (this.current % this.scale == 0) {
        this.scaledCurrent++;
      }
      this.show();
    }

    private void show () {
      System.out.print("[");
      for (long i = 0; i < this.scaledCurrent; i++) {
        System.out.print("=");
      }
      for (long i = this.scaledCurrent + 1; i < 50; i++) {
        System.out.print(" ");
      }
      if (this.scaledCurrent >= 50) {
        System.out.println("] - Done!");
      } else {
        System.out.print("]\r");
      }
    }
  }

}
