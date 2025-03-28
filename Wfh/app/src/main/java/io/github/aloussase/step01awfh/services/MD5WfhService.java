package io.github.aloussase.step01awfh.services;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class MD5WfhService implements WfhService {
    private static final Logger logger = Logger.getLogger(MD5WfhService.class.getName());

    @Override
    public byte[] hash(String contents) {
        return hash(contents.getBytes());
    }

    @Override
    public byte[] hash(byte[] contents) {
        logger.info("Hashing contents");
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(contents);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.warning("Failed to hash contents");
            return null;
        }
    }

    @Override
    public String hashToString(String contents) {
        return DigestUtils.md5Hex(contents);
    }
}
