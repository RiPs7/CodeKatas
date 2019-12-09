package main;

import java.util.Arrays;
import java.util.List;

import static main.MainUtils.Print.ANSI_CYAN;
import static main.MainUtils.Print.ANSI_RESET;

public class Main {

    private static final String[] RESOURCES = new String[] {
        "JavaClass1.txt",
        "JavaClass2.txt",
        "JavaClass3.txt"
    };

    public static void main (String[] args) {
        Arrays.stream(RESOURCES)
            .forEach(resource -> {
                final JavaParser javaParser = new JavaParser(resource);
                final List<String> codeLines = javaParser.findCodeLines();
                if (codeLines == null) {
                    System.out.println("Could not read " + resource);
                } else {
                    System.out.println(
                        ANSI_CYAN.getCode() + resource + " has the following " + codeLines.size() + " lines of code:" +
                            ANSI_RESET.getCode());
                    MainUtils.prettyPrintList(codeLines);
                    System.out.println();
                }
            });
    }
}
