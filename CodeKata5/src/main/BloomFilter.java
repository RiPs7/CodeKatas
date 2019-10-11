package main;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

class BloomFilter {

    private boolean[] bits;
    private MessageDigest messageDigest;
    private int indexingChunkSize;

    BloomFilter (final BloomFilterAlgorithm algorithm) {
        bits = new boolean[algorithm.getBloomFilterSize()];
        messageDigest = algorithm.getMessageDigest();
        indexingChunkSize = (int)log2(algorithm.getBloomFilterSize()) / 8;
    }

    void addElement (final String element) {
        final byte[] hash = messageDigest.digest(element.getBytes());
        final List<byte[]> chunks = chunk(hash, indexingChunkSize);
        for (final byte[] chunk : chunks) {
            final int index = new BigInteger(1, chunk).intValue();
            bits[index] = true;
        }
    }

    boolean containsElement (final String element) {
        final byte[] hash = messageDigest.digest(element.getBytes());
        final List<byte[]> chunks = chunk(hash, indexingChunkSize);
        for (final byte[] chunk : chunks) {
            final int index = new BigInteger(1, chunk).intValue();
            if (!bits[index]) {
                return false;
            }
        }
        return true;
    }

    private List<byte[]> chunk (final byte[] input, final int chunkSize) {
        if (chunkSize <= 0) {
            throw new InvalidParameterException("Chunk size must be greater that 0");
        }
        return IntStream.iterate(0, i -> i + chunkSize)
            .limit((long) Math.ceil((double) input.length / chunkSize))
            .mapToObj(j -> Arrays.copyOfRange(input, j, Math.min(j + chunkSize, input.length)))
            .collect(toList());
    }

    private double log2 (final int x) {
        return Math.log10(x) / Math.log10(2);
    }

}
