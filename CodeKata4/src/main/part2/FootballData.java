package main.part2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * The file football.dat contains the results from the English Premier League for 2001/2. The columns labeled ‘F’
 * and ‘A’ contain the total number of goals scored for and against each team in that season (so Arsenal scored 79
 * goals against opponents, and had 36 goals scored against them). Write a program to print the name of the team
 * with the smallest difference in ‘for’ and ‘against’ goals.
 */
public class FootballData {

    // The resource filename.
    private static final String RESOURCE_FILENAME = "res/football.dat";

    // The tabular data delimiter (sequence of spaces / tabs).
    private static final Pattern TABULAR_DATA_DELIMITER = Pattern.compile("[\\s\\t]+");

    public static void main (String[] args) {
        // Get the current starting time.
        final long start = System.currentTimeMillis();

        // All the required data for comparison and output (incl. headers).
        final List<String[]> footballData = readTabularDataAtCols(1, 6, 8);
        if (footballData == null) {
            System.out.println("Could not read football data. Exiting...");
            System.exit(-1);
        }

        // The data for comparison and output (excl. headers).
        final List<String[]> footballDataRecords = footballData.subList(1, footballData.size() - 1);

        // Prepare data for comparison.
        // 1. Keep the lines that have 3 elements.
        // 2. For every data line...
        // 3. Get only the columns required for comparison.
        // 4. Get the elements mapping to these columns.
        // 5. Convert them to double. If any contains '*', remove it and convert.
        // 6. Keep the lines that have 2 elements.
        // 7. Return them as a list.
        final List<double[]> numericFootballData = footballDataRecords.stream()
            .filter(dataLine -> dataLine.length == 3)
            .map(dataLine -> IntStream.of(1, 2)
                .mapToObj(col -> dataLine[col])
                .mapToDouble(element -> {
                    try {
                        return Double.parseDouble(element);
                    } catch (NumberFormatException nfe) {
                        return Double.parseDouble(element.substring(0, element.length() - 1));
                    }
                })
                .toArray())
            .filter(dataLine -> dataLine.length == 2)
            .collect(toList());

        // Carry out the comparison and find the corresponding index (if none, throw exception)
        // 1. Perform a reduce operation over the pair of indices.
        // 2. Get the corresponding data lines.
        // 3. Return the index of the data line where the distance of its two elements is the lowest.
        // 4. Return the overall index of the data line with the minimum distance. If no such value, throw exception.
        final int minRecordIndex = IntStream.range(0, numericFootballData.size())
            .reduce((i, j) -> {
                final double[] dataLine1 = numericFootballData.get(i);
                final double[] dataLine2 = numericFootballData.get(j);
                return Math.abs(dataLine1[0] - dataLine1[1]) < Math.abs(dataLine2[0] - dataLine2[1]) ? i : j;
            })
            .orElseThrow(() -> new IllegalArgumentException("Could not find the minimum record."));

        // Print the minimum distance.
        System.out.println(
            footballDataRecords.get(minRecordIndex)[0] + " --- " + footballDataRecords.get(minRecordIndex)[1] + " " +
                footballDataRecords.get(minRecordIndex)[2]);

        // Print the performance time.
        System.out.println("It took: " + (System.currentTimeMillis() - start) + " ms");
    }

    private static List<String[]> readTabularDataAtCols (Integer... cols) {
        // Get the resource as an input stream.
        final InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(RESOURCE_FILENAME);
        if (inputStream == null) {
            return null;
        }
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final BufferedReader reader = new BufferedReader(inputStreamReader);
        // For every line...
        // 1. Split the data based on the tabular data delimiter.
        // 2. Get only the columns specified.
        // 3. Map the column integer to the corresponding element.
        // 4. Return the result as a list of string array.
        return reader.lines()
            .map(line -> {
                final String[] lineData = TABULAR_DATA_DELIMITER.split(line.trim());
                // filter only the required columns and map them to the actual data.
                return IntStream.range(0, lineData.length)
                    .filter(Arrays.asList(cols)::contains)
                    .mapToObj(col -> lineData[col])
                    .toArray(String[]::new);
            })
            .collect(toList());
    }

}
