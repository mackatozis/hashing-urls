package eu.mackatozis.hashing.urls.service;

import eu.mackatozis.hashing.urls.model.UrlHashPrefixes;

import java.net.MalformedURLException;

public interface UrlHashPrefixesService {

    UrlHashPrefixes findUrlHashPrefixes(String url) throws MalformedURLException;
}
