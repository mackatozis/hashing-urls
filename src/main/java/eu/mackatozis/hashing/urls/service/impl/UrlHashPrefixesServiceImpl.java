package eu.mackatozis.hashing.urls.service.impl;

import eu.mackatozis.hashing.urls.constants.HashPrefixLength;
import eu.mackatozis.hashing.urls.model.ExpressionHashes;
import eu.mackatozis.hashing.urls.model.UrlHashPrefixes;
import eu.mackatozis.hashing.urls.service.UrlHashPrefixesService;
import eu.mackatozis.hashing.urls.service.CanonicalizationService;
import eu.mackatozis.hashing.urls.service.ExpressionExtractionService;
import eu.mackatozis.hashing.urls.service.HashComputationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class deals with finding all the possible hash prefixes for all the
 * possible host-suffix and path-prefix expressions of a URL {@code String}.
 */
@Service
@RequiredArgsConstructor
public class UrlHashPrefixesServiceImpl implements UrlHashPrefixesService {

    private final CanonicalizationService canonicalizationService;

    private final ExpressionExtractionService expressionExtractionService;

    private final HashComputationService hashComputationService;

    /**
     * Finds all hash prefixes of the input URL {@code String} by:
     *
     * <ul>
     *   <li>Canonicalizing the URL
     *   <li>Creating all the possible host-suffix and path-prefix for the URL
     *   <li>Computing the full-length hash for all possible host-suffix
     *       and path-prefix
     *   <li>Computing the hash prefix for each full-length hash
     * </ul>
     *
     * @param url URL {@code String} to find hash prefixes
     * @return URL hash prefixes
     * @throws MalformedURLException when supplied URL {@code String} is
     *         a malformed URL
     */
    @Override
    public UrlHashPrefixes findUrlHashPrefixes(String url) throws MalformedURLException {
        if (StringUtils.isNotBlank(url)) {

            Set<ExpressionHashes> expressionHashes = new HashSet<>();
            List<String> hashPrefixes = new ArrayList<>();
            String fullHash;

            String canonicalUrl = canonicalizationService.canonicalizeUrl(url);
            Set<String> expressions = expressionExtractionService.extractExpressions(canonicalUrl);

            for (String expression : expressions) {
                fullHash = hashComputationService.computeSha256Hash(expression);

                for (int i = HashPrefixLength.MIN_HASH_PREFIX_LENGTH;
                     i < HashPrefixLength.MAX_HASH_PREFIX_LENGTH;
                     i++) {
                    hashPrefixes.add(hashComputationService.computeSha256HashPrefix(fullHash, i));
                }
                expressionHashes.add(
                        ExpressionHashes.builder()
                                .expression(expression)
                                .fullHash(fullHash)
                                .hashPrefixes(new ArrayList<>(hashPrefixes))
                                .build());
                hashPrefixes.clear();
            }
            return UrlHashPrefixes.builder().url(url).expressionHashes(expressionHashes).build();
        } else {
            throw new MalformedURLException("The url should not be empty");
        }
    }
}
