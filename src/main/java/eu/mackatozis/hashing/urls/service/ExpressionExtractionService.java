package eu.mackatozis.hashing.urls.service;

import java.util.Set;

public interface ExpressionExtractionService {

    Set<String> extractExpressions(String url);
}
