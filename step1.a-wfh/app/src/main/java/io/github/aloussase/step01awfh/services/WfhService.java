package io.github.aloussase.step01awfh.services;

public interface WfhService {
    byte[] hash(String contents);

    byte[] hash(byte[] contents);

    String hashToString(String contents);
}
