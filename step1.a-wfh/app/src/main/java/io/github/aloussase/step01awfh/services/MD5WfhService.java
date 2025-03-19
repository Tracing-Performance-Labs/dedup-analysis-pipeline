package io.github.aloussase.step01awfh.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5WfhService implements WfhService {
    @Override
    public byte[] hash(String contents) {
        return hash(contents.getBytes());
    }

    @Override
    public byte[] hash(byte[] contents) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(contents);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
