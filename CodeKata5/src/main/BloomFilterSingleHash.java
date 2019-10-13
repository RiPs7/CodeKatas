package main;

import java.math.BigInteger;
import java.util.List;

class BloomFilterSingleHash extends BloomFilter {

    BloomFilterSingleHash (final BloomFilterAlgorithm algorithm) {
        bits = new boolean[algorithm.getBloomFilterSize()];
        messageDigest = algorithm.getMessageDigest();
        indexingChunkSize = (int)log2(algorithm.getBloomFilterSize()) / 8;
    }

    @Override
    void addElement (final String element) {
        final byte[] hash = messageDigest.digest(element.getBytes());
        final List<byte[]> chunks = chunk(hash, indexingChunkSize);
        for (final byte[] chunk : chunks) {
            final int index = new BigInteger(1, chunk).intValue();
            bits[index] = true;
        }
    }

    @Override
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

}
