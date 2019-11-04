package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
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
    return reader.lines().collect(Collectors.toList());
  }

  static void readFromInputAndApplyFunction (String promptMessage, Consumer<String> function) {
    System.out.println(promptMessage);
    final Scanner scanner = new Scanner(System.in);
    String line;
    while (!(line = scanner.nextLine()).equals("")) {
      function.accept(line);
    }
  }

}
