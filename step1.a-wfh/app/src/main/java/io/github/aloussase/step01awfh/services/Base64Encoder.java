package io.github.aloussase.step01awfh.services;

import java.util.Base64;
import java.util.logging.Logger;

public class Base64Encoder implements PlainTextEncoder {
    private static final Logger logger = Logger.getLogger(Base64Encoder.class.getName());

    @Override
    public String encode(byte[] buffer) {
        logger.info("Encoding bytes to base64");
        return Base64.getEncoder().encodeToString(buffer);
    }
}
