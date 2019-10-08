package main.part3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Take the two programs written previously and factor out as much common code as possible, leaving you with two
 * smaller programs and some kind of shared functionality.
 *
 * QUESTIONS:
 * 1. To what extent did the design decisions you made when writing the original programs make it easier or harder to
 * factor out common code?
 *
 * 2. Was the way you wrote the second program influenced by writing the first?
 *
 * 3. Is factoring out as much common code as possible always a good thing? Did the readability of the programs suffer
 * because of this requirement? How about the maintainability?

 */
class ModelDataHelper {

    private static final Pattern TABULAR_DATA_DELIMITER = Pattern.compile("[\\s\\t]+");

    void solveProblem (String resource, Integer headerOffset, Integer... cols) {
        // Get the current starting time.
        final long start = System.currentTimeMillis();

        // All the required data for comparison and output (incl. headers).
        final List<String[]> data = readTabularDataAtCols(resource, cols);
        if (data == null) {
            System.out.println("Could not read weather data. Exiting...");
            System.exit(-1);
        }

        // The data for comparison and output (excl. headers).
        final List<String[]> dataRecords = data.subList(headerOffset, data.size() - 1);

        // Prepare data for comparison.
        // 1. Keep the lines that have 3 elements.
        // 2. For every data line...
        // 3. Get only the columns required for comparison.
        // 4. Get the elements mapping to these columns.
        // 5. Convert them to double. If any contains '*', remove it and convert.
        // 6. Keep the lines that have 2 elements.
        // 7. Return them as a list.
        final List<double[]> numericWeatherData = dataRecords.stream()
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
        final int minRecordIndex = IntStream.range(0, numericWeatherData.size())
            .reduce((i, j) -> {
                final double[] dataLine1 = numericWeatherData.get(i);
                final double[] dataLine2 = numericWeatherData.get(j);
                return Math.abs(dataLine1[0] - dataLine1[1]) < Math.abs(dataLine2[0] - dataLine2[1]) ? i : j;
            })
            .orElseThrow(() -> new IllegalArgumentException("Could not find the minimum record."));

        // Print the minimum distance.
        System.out.println(
            dataRecords.get(minRecordIndex)[0] + " --- " + dataRecords.get(minRecordIndex)[1] + " " +
                dataRecords.get(minRecordIndex)[2]);

        // Print the performance time.
        System.out.println("It took: " + (System.currentTimeMillis() - start) + " ms");
    }

    private List<String[]> readTabularDataAtCols (String fileName, Integer... cols) {
        // Get the resource as an input stream.
        final InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
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

/*
 * ANSWERS:
 *
 * 1. To what extent did the design decisions you made when writing the original programs make it easier or harder to
 * factor out common code?
 * - By organizing the original programs into small separate functions based on the functionality, it was easy to
 * integrate them and move them into the Helper, by finding which parts of them are parameterized and can be used
 * as input arguments.
 * One function was used to load the data from the files, whereas the main logic of handling it and carrying out the
 * logic for each problem was adjusted in the main code.
 * This made it slightly hard to extract the common code out but also keep the general functionality in place.
 * The similar code once again was identified and the parameterizable factors were used as argument inputs.
 * Hence, splitting the functionality down to individual methods helped as they could be re-used without a change.
 * However, keeping the basic functionality of the problems in one piece, made it easy to keep track and manage, but
 * didn't help when the two pieces of code had to be merged.
 *
 * 2. Was the way you wrote the second program influenced by writing the first?
 * - Yes, after getting the main functionality working on the first one it was easy to amend it and adjust it according
 * to the requirements of the second. Since the two problems had similar contexts, the idea was to get the main code
 * working for the first one and the second would be easily adjustable. Due to these similarities, the code was pretty
 * much a copy-paste and slighlty changed for the second one.
 *
 * 3. Is factoring out as much common code as possible always a good thing? Did the readability of the programs suffer
 * because of this requirement? How about the maintainability?
 * - In this case, I tried to factor out as much code as possible, which turned out be as much as leaving only one line
 * of code in the main programs. This makes the readability slightly worse in my opinion, so a possible
 * code-related solution could be to attempt to break down the 'solveProblem' into smaller chunks - functions and call
 * them from the main program. Exposing more functions to the main program could have both advantages (such as
 * readability) and disadvantages (such as maintainability). In these two problems, the scope of the code is not large
 * enough to affect maintainability, but it could potentially affect it in a more scalable scenario.
 * I would say that when having to read files and handle data from them, it is a good practice to get the general code
 * individually developed so it can be re-used, but we need to be aware of the boundaries of making the code unreadable.
 * However, when we want to focus on maintainability and need to deal with risky code, splitting it down to individually
 * maintainable code which can be re-used can help us identify faulty code.
 */