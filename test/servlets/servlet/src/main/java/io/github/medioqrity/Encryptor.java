package io.github.medioqrity;

import java.util.Base64;

public class Encryptor {
    public static String encode(String target) {
        byte[] encodedBytes = Base64.getEncoder().encode(target.getBytes());
        return new String(encodedBytes);
    }

    public static String decode(String target) {
        byte[] decodedBytes = Base64.getDecoder().decode(target);
        return new String(decodedBytes);
    }
}