package main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainUtils {

  private static List<String> readLinesFromResources (String filename) throws FileNotFoundException {
    // Get the resource as an input stream.
    final InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("res/" + filename);
    if (inputStream == null) {
      throw new FileNotFoundException();
    }
    final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    final BufferedReader reader = new BufferedReader(inputStreamReader);

    // Read all the lines
    return reader.lines().collect(Collectors.toList());
  }

  public static JSONObject readJsonObjectFromResources (String filename) throws FileNotFoundException, ParseException {
    final String fileContent = readLinesFromResources(filename).stream().reduce("", String::concat);
    return (JSONObject) new JSONParser().parse(fileContent);
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
