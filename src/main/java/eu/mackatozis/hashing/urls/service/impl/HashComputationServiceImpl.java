package eu.mackatozis.hashing.urls.service.impl;

import eu.mackatozis.hashing.urls.constants.HashPrefixLength;
import eu.mackatozis.hashing.urls.service.HashComputationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * This class deals with computing hash prefixes of all the possible
 * host-suffix and path-prefix expressions of a URL.
 *
 * A hash prefix represents a SHA256 hash. It may either be full, where
 *
 * hashPrefix.length() == {@link
 * HashPrefixLength#MAX_HASH_PREFIX_LENGTH}
 *
 * or be partial, where
 *
 * hashPrefix.length() == {@link
 * HashPrefixLength#MIN_HASH_PREFIX_LENGTH}
 *
 * It also computed the full hash for the given URL host-suffix and
 * path-prefix expression.
 * </pre>
 */
@Service
public class HashComputationServiceImpl implements HashComputationService {

    /**
     * Computes the SHA256 hash prefix by the most significant [4-32] bytes
     * of the input SHA256-formatted hash {@code String}.
     *
     * @param sha256hash hash to compute prefix for
     * @param prefixSize the number of bytes
     * @return SHA256 hash prefix {@code String}
     */
    @Override
    public String computeSha256HashPrefix(String sha256hash, int prefixSize) {
        if (prefixSize >= HashPrefixLength.MIN_HASH_PREFIX_LENGTH
                && prefixSize <= HashPrefixLength.MAX_HASH_PREFIX_LENGTH) {
            return sha256hash.substring(0, prefixSize * 2);
        } else {
            throw new IllegalArgumentException(
                    "Prefix size of a hash prefix should be between 4 and 32 bytes");
        }
    }

    /**
     * Computes the full-length SHA256-formatted hash for the
     * input expression {@code String}.
     *
     * @param expression expression to compute full-length hash for
     * @return full-length SHA256-formatted hash {@code String}
     */
    @Override
    public String computeSha256Hash(String expression) {
        return DigestUtils.sha256Hex(expression);
    }
}
