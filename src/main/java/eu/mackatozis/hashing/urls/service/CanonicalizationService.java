package eu.mackatozis.hashing.urls.service;

import java.net.MalformedURLException;

public interface CanonicalizationService {

    String canonicalizeUrl(String url) throws MalformedURLException;
}
