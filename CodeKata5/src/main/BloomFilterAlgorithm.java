package main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum BloomFilterAlgorithm {

    SHA256("SHA-256", 256), // 256 spaces => log2(256) = 8 bits = 1 byte for indexing

    SHA512("SHA-512", 256 * 256), // 65536 spaces => log2(65536) = 16 bits = 2 bytes for indexing

    SHA512_EXT("SHA-512", 256 * 256 * 256); // 16777216 spaces => log2(16777216) = 24 bits = 3 bytes for indexing

    private MessageDigest messageDigest;

    private int bloomFilterSize;

    BloomFilterAlgorithm (final String messageDigest, final int bloomFilterSize) {
        try {
            this.messageDigest = MessageDigest.getInstance(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.bloomFilterSize = bloomFilterSize;
    }

    public MessageDigest getMessageDigest () {
        return messageDigest;
    }

    public int getBloomFilterSize () {
        return bloomFilterSize;
    }
}
