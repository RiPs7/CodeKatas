package main;

import java.math.BigInteger;
import java.util.List;

class BloomFilterMultiHash extends BloomFilter {

    BloomFilterMultiHash (final BloomFilterAlgorithm algorithm) {
        bits = new boolean[algorithm.getBloomFilterSize()];
        messageDigest = algorithm.getMessageDigest();
        indexingChunkSize = (int)log2(algorithm.getBloomFilterSize()) / 8;
        if (indexingChunkSize < 2) {
            throw new IllegalArgumentException("Algorithm not supported for multi-hash bloom filter.");
        }
    }

    @Override
    void addElement (final String element) {
        final byte[] hash = messageDigest.digest(element.getBytes());
        final List<byte[]> chunks = chunk(hash, indexingChunkSize);
        for (final byte[] chunk : chunks) {
            final List<byte[]> semiChunks = chunk(chunk, indexingChunkSize / 2);
            for (final byte[] semiChunk : semiChunks) {
                final int index = new BigInteger(1, semiChunk).intValue();
                bits[index] = true;
            }
        }
    }

    @Override
    boolean containsElement (final String element) {
        final byte[] hash = messageDigest.digest(element.getBytes());
        final List<byte[]> chunks = chunk(hash, indexingChunkSize);
        for (final byte[] chunk : chunks) {
            final List<byte[]> semiChunks = chunk(chunk, indexingChunkSize / 2);
            for (final byte[] semiChunk : semiChunks) {
                final int index = new BigInteger(1, semiChunk).intValue();
                if (!bits[index]) {
                    return false;
                }
            }
        }
        return true;
    }

}
