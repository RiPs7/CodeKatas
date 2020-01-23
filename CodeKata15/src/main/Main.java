package main;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * The Fibbinary numbers are the numbers that their binary representation does not contain any adjacent 1's.
 * The pattern of the number of the Fibbinary integers follows the Fibonacci sequence. This means that:
 * If F[x] is the function of the number of Fibbinary integers of size x, then
 * F[x+1] = F[x] + F[x-1].
 * e.g. F[5] = F[4] + F[3] => 13 = 8 + 5
 * <p>
 * Proof:
 * Assuming that F[x] = f. (e.g. x = 3 => F[3] = 5)
 * Incrementing the size to x+1 (4), we will have all the previous numbers twice, once with a leading 0 and once with a
 * leading 1.
 * The ones with the leading 0 contain f Fibbinary integers (5) as this 0 does not affect the adjacency of 1s in the
 * rest of the number.
 * For the ones with the leading 1 we have the following two cases:
 * 1) The first half of them have a 0 as the second digit, hence the same number of Fibbinary integers as those with
 * size x-1 (2) as the leading part is 10...
 * 2) The second half of them have a 1 as the second digit, hence do not contribute any Fibbinary integers as the
 * leading part is 11...
 * Overall, we can see that we have for F[x+1] = F[x] + F[x-1]
 *
 * Example:
 * 00      000      0 000
 * 01      001      0 001
 * 10      010      0 010
 * 11      011      0 011
 * f=3     100      0 100
 *         101      0 101
 *         110      0 110
 *         111      0 111
 *         f=5      f=5
 *
 *                  1 0 00
 *                  1 0 01
 *                  1 0 10
 *                  1 0 11
 *                  f=5+3=8
 *                  <p>
 *                  1 1 00
 *                  1 1 01
 *                  1 1 10
 *                  1 1 11
 *                  f=8
 */

public class Main {

    private static final int SIZE_LIMIT = 15; // must be less than ~30

    private static final boolean PRINT_FIBBINARIES = false;

    public static void main (String[] args) {
        final Map<Integer, List<Integer>> allFibbinaries = IntStream.range(1, SIZE_LIMIT)
            .boxed()
            .map(i -> new AbstractMap.SimpleEntry<>(i, getFibbinaryNumbersForSize(i)))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<Integer, List<Integer>> entry : allFibbinaries.entrySet()) {
            System.out.println(
                "Number of Fibbinary integers for size: " + entry.getKey() + " = " + entry.getValue().size());
        }

        if (PRINT_FIBBINARIES) {
            System.out.println();
            for (Map.Entry<Integer, List<Integer>> entry : allFibbinaries.entrySet()) {
                System.out.println(
                    "The " + entry.getValue().size() + " Fibbinary integers for size: " + entry.getKey() + ": " +
                        entry.getValue().stream().map(Main::toBinary).collect(toList()));
            }
        }
    }

    private static List<Integer> getFibbinaryNumbersForSize (int size) {
        return IntStream.range(0, (int) Math.pow(2, size))
            .boxed()
            .filter(Main::isFibbinary)
            .collect(Collectors.toList());
    }

    /**
     * For any arbitrary integer, we shift the bits to the right by one and apply a bit-wise operation.
     * If the result is 0, then there are no consecutive 1's, otherwise there are.
     * <p>
     * For example:
     * Integer 11:
     *      1011
     *    & _101
     *    = 0001
     * Integer 42
     *    101010
     *  & _10101
     *  = 000000
     * </p>
     *
     * @param n The integer n
     * @return If it is fibbinary
     */
    private static boolean isFibbinary (int n) {
        return (n & (n >> 1)) == 0;
    }

    /**
     * Returns the binary representation of a given integer
     *
     * @param n The given integer
     * @return The binary representation
     */
    private static String toBinary (int n) {
        return Integer.toBinaryString(n);
    }
}
