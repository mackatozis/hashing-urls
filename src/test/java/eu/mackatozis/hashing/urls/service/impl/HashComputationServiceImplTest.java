package eu.mackatozis.hashing.urls.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class HashComputationServiceImplTest {

    @Autowired
    private HashComputationServiceImpl hashComputationServiceImpl;

    @Test
    public void computeSha256HashPrefix_B1_FIPS_180_2_shouldBeOk() {
        String sha256Hash = hashComputationServiceImpl.computeSha256Hash("abc");

        assertEquals("ba7816bf", hashComputationServiceImpl.computeSha256HashPrefix(sha256Hash, 4));
    }

    @Test
    public void computeSha256HashPrefix_B2_FIPS_180_2_shouldBeOk() {
        String sha256Hash =
                hashComputationServiceImpl.computeSha256Hash(
                        "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq");

        assertEquals(
                "248d6a61d206", hashComputationServiceImpl.computeSha256HashPrefix(sha256Hash, 6));
    }

    @Test
    public void computeSha256HashPrefix_B3_FIPS_180_2_shouldBeOk() {
        String input = StringUtils.repeat('a', 1000000);
        String sha256Hash = hashComputationServiceImpl.computeSha256Hash(input);

        assertEquals(
                "cdc76e5c9914fb9281a1c7e2",
                hashComputationServiceImpl.computeSha256HashPrefix(sha256Hash, 12));
    }

    @Test
    public void computeSha256HashPrefix_withSignificantBytes_3_OutOfRange_shouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> hashComputationServiceImpl.computeSha256HashPrefix("abc", 3));
    }

    @Test
    public void computeSha256HashPrefix_withSignificantBytes_33_OutOfRange_shouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> hashComputationServiceImpl.computeSha256HashPrefix("abc", 33));
    }

    @Test
    public void computeFullHash_B1_FIPS_180_2_shouldBeOk() {
        assertEquals(
                "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                hashComputationServiceImpl.computeSha256Hash("abc"));
    }

    @Test
    public void computeFullHash_B2_FIPS_180_2_shouldBeOk() {
        assertEquals(
                "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
                hashComputationServiceImpl.computeSha256Hash(
                        "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"));
    }

    @Test
    public void computeFullHash_B3_FIPS_180_2_shouldBeOk() {
        String input = StringUtils.repeat('a', 1000000);

        assertEquals(
                "cdc76e5c9914fb9281a1c7e284d73e67f1809a48a497200e046d39ccc7112cd0",
                hashComputationServiceImpl.computeSha256Hash(input));
    }
}
