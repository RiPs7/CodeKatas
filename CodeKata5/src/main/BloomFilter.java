package main;

import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

abstract class BloomFilter {

    boolean[] bits;
    MessageDigest messageDigest;
    int indexingChunkSize;

    abstract void addElement (final String element);

    abstract boolean containsElement (final String element);

    List<byte[]> chunk (final byte[] input, final int chunkSize) {
        if (chunkSize <= 0) {
            throw new InvalidParameterException("Chunk size must be greater that 0");
        }
        return IntStream.iterate(0, i -> i + chunkSize)
            .limit((long) Math.ceil((double) input.length / chunkSize))
            .mapToObj(j -> Arrays.copyOfRange(input, j, Math.min(j + chunkSize, input.length)))
            .collect(toList());
    }

    double log2 (final int x) {
        return Math.log10(x) / Math.log10(2);
    }

}
