package eu.mackatozis.hashing.urls.service;

public interface HashComputationService {

    String computeSha256HashPrefix(String sha256hash, int significantBytes);

    String computeSha256Hash(String expression);
}
