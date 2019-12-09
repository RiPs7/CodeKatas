package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static main.MainUtils.Print.ANSI_RESET;
import static main.MainUtils.Print.UNDERLINE;

class MainUtils {

  static List<String> readLinesFromResources (String filename) {
    // Get the resource as an input stream.
    final InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("res/" + filename);
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
      System.out.println(promptMessage);
    }
  }

  static void prettyPrintList (List<String> list) {
    list.forEach(System.out::println);
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

  public enum Print {
    ANSI_RESET("\u001B[0m"),
    BOLD("\u001B[1m"),
    ITALICS("\u001B[3m"),
    UNDERLINE("\u001B[4m"),
    STRIKETHROUGH("\u001B[9m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_WHITE("\u001B[37m"),
    ANSI_BRIGHT_BLACK("\u001B[90m"),
    ANSI_BRIGHT_RED("\u001B[91m"),
    ANSI_BRIGHT_GREEN("\u001B[92m"),
    ANSI_BRIGHT_YELLOW("\u001B[93m"),
    ANSI_BRIGHT_BLUE("\u001B[94m"),
    ANSI_BRIGHT_PURPLE("\u001B[95m"),
    ANSI_BRIGHT_CYAN("\u001B[96m"),
    ANSI_BRIGHT_WHITE("\u001B[97m"),
    ANSI_BG_BLACK("\u001B[40m"),
    ANSI_BG_RED("\u001B[41m"),
    ANSI_BG_GREEN("\u001B[42m"),
    ANSI_BG_YELLOW("\u001B[43m"),
    ANSI_BG_BLUE("\u001B[44m"),
    ANSI_BG_PURPLE("\u001B[45m"),
    ANSI_BG_CYAN("\u001B[46m"),
    ANSI_BG_WHITE("\u001B[47m"),
    ANSI_BRIGHT_BG_BLACK("\u001B[100m"),
    ANSI_BRIGHT_BG_RED("\u001B[101m"),
    ANSI_BRIGHT_BG_GREEN("\u001B[102m"),
    ANSI_BRIGHT_BG_YELLOW("\u001B[103m"),
    ANSI_BRIGHT_BG_BLUE("\u001B[104m"),
    ANSI_BRIGHT_BG_PURPLE("\u001B[105m"),
    ANSI_BRIGHT_BG_CYAN("\u001B[106m"),
    ANSI_BRIGHT_BG_WHITE("\u001B[107m");

    private String code;

    Print (String code) {
      this.code = code;
    }

    public String getCode () {
      return code;
    }
  }

}
