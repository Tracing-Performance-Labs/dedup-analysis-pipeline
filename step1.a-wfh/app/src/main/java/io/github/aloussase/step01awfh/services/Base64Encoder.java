package io.github.aloussase.step01awfh.services;

import java.util.Base64;

public class Base64Encoder implements PlainTextEncoder {
    @Override
    public String encode(byte[] buffer) {
        return Base64.getEncoder().encodeToString(buffer);
    }
}
