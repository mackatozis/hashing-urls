package eu.mackatozis.hashing.urls.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IpUtilsTest {

    private String decimalIp = "192.168.0.1";
    private String octalIp = "0300.0250.00.0001";
    private String hexIp = "0xc0.0xa8.0x0.0x01";
    private String integerIp = "3232235521";

    @Test
    public void isIpAddress_shouldBeOk() {
        assertTrue(IpUtils.isIpAddress(decimalIp));
        assertTrue(IpUtils.isIpAddress(octalIp));
        assertTrue(IpUtils.isIpAddress(hexIp));
        assertTrue(IpUtils.isIpAddress(integerIp));
    }

    @Test
    public void isDecimalIpAddress_shouldBeOk() {
        assertTrue(IpUtils.isDecimalIpAddress(decimalIp));
        assertFalse(IpUtils.isDecimalIpAddress(octalIp));
        assertFalse(IpUtils.isDecimalIpAddress(hexIp));
        assertFalse(IpUtils.isDecimalIpAddress(integerIp));
    }

    @Test
    public void isOctalEncodedIpAddress_shouldBeOk() {
        assertFalse(IpUtils.isOctalEncodedIpAddress(decimalIp));
        assertTrue(IpUtils.isOctalEncodedIpAddress(octalIp));
        assertFalse(IpUtils.isOctalEncodedIpAddress(hexIp));
        assertFalse(IpUtils.isOctalEncodedIpAddress(integerIp));
    }

    @Test
    public void isHexEncodedIpAddress_shouldBeOk() {
        assertFalse(IpUtils.isHexEncodedIpAddress(decimalIp));
        assertFalse(IpUtils.isHexEncodedIpAddress(octalIp));
        assertTrue(IpUtils.isHexEncodedIpAddress(hexIp));
        assertFalse(IpUtils.isHexEncodedIpAddress(integerIp));
    }

    @Test
    public void isNumericIpAddress_shouldBeOk() {
        assertFalse(IpUtils.isIntegerIpAddress(decimalIp));
        assertFalse(IpUtils.isIntegerIpAddress(octalIp));
        assertFalse(IpUtils.isIntegerIpAddress(hexIp));
        assertTrue(IpUtils.isIntegerIpAddress(integerIp));
    }

    @Test
    public void resolveDecimalIpAddress_shouldBeOk() {
        String ipAddress = "192.168.0.1";

        assertEquals(ipAddress, IpUtils.resolveDecimalIpAddress(decimalIp));
        assertEquals(ipAddress, IpUtils.resolveDecimalIpAddress(octalIp));
        assertEquals(ipAddress, IpUtils.resolveDecimalIpAddress(hexIp));
        assertEquals(ipAddress, IpUtils.resolveDecimalIpAddress(integerIp));
        assertNull(IpUtils.resolveDecimalIpAddress(RandomStringUtils.random(10, true, false)));
    }
}
