package eu.mackatozis.hashing.urls.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HashPrefixLength {

    public static final int MIN_HASH_PREFIX_LENGTH = 4;

    public static final int MAX_HASH_PREFIX_LENGTH = 32;
}
