package eu.mackatozis.hashing.urls.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CanonicalizationServiceImplTest {

    @Autowired
    private CanonicalizationServiceImpl canonicalizationServiceImpl;

    @Test
    public void canonicalizeUrl_withUnicodeCharactersInHost_shouldBeOk()
            throws MalformedURLException {
        assertEquals(
                "http://xn--hxarsa5b.com/",
                canonicalizationServiceImpl.canonicalizeUrl("  http://ελλάς.com/"));
    }

    @Test
    public void canonicalizeUrl_withLeadingTrailingWhitespaces_shouldBeOk()
            throws MalformedURLException {
        assertEquals(
                "http://www.google.com/",
                canonicalizationServiceImpl.canonicalizeUrl("  http://www.google.com/  "));
    }

    @Test
    public void canonicalizeUrl_withEncodedUrl_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://host/%25",
                canonicalizationServiceImpl.canonicalizeUrl("http://host/%25%32%35"));
        assertEquals(
                "http://host/%25%25",
                canonicalizationServiceImpl.canonicalizeUrl("http://host/%25%32%35%25%32%35"));
        assertEquals(
                "http://host/%25",
                canonicalizationServiceImpl.canonicalizeUrl("http://host/%2525252525252525"));
        assertEquals(
                "http://168.188.99.26/.secure/www.ebay.com/",
                canonicalizationServiceImpl.canonicalizeUrl(
                        "http://%31%36%38%2e%31%38%38%2e%39%39%2e%32%36/%2E%73%65%63%75%72%65/%77%77%77%2E%65%62%61%79%2E%63%6F%6D/"));
        assertEquals(
                "http://host%23.com/~a!b@c%23d$e%25f^00&11*22(33)44_55+",
                canonicalizationServiceImpl.canonicalizeUrl(
                        "http://host%23.com/%257Ea%2521b%2540c%2523d%2524e%25f%255E00%252611%252A22%252833%252944_55%252B"));
    }

    @Test
    public void canonicalizeUrl_withConsecutiveDots_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://www.google.com/",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.google.com.../"));
        assertEquals(
                "http://www.google.com/",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.google.com/blah/.."));
    }

    @Test
    public void canonicalizeUrl_withUppercaseCharacters_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://www.google.com/",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.GOOgle.com/"));
    }

    @Test
    public void canonicalizeUrl_withSchemeMissing_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://www.google.com/",
                canonicalizationServiceImpl.canonicalizeUrl("www.google.com/"));
    }

    @Test
    public void canonicalizeUrl_withSchemeMissingAndNoTrailingSlash_shouldBeOk()
            throws MalformedURLException {
        assertEquals(
                "http://www.google.com/",
                canonicalizationServiceImpl.canonicalizeUrl("www.google.com"));
    }

    @Test
    public void canonicalizeUrl_withNoTrailingSlash_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://notrailingslash.com/",
                canonicalizationServiceImpl.canonicalizeUrl("http://notrailingslash.com"));
    }

    @Test
    public void canonicalizeUrl_withEmbeddedTabAndCRAndLF_shouldBeOk()
            throws MalformedURLException {
        assertEquals(
                "http://www.google.com/foobarbaz2",
                canonicalizationServiceImpl.canonicalizeUrl(
                        "http://www.google.com/foo\tbar\rbaz\n2"));
    }

    @Test
    public void canonicalizeUrl_withFragment_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://www.evil.com/blah",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.evil.com/blah#frag"));
        assertEquals(
                "http://evil.com/foo",
                canonicalizationServiceImpl.canonicalizeUrl("http://evil.com/foo#bar#baz"));
    }

    @Test
    public void canonicalizeUrl_withIntegerIpAddress_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://195.127.0.11/blah",
                canonicalizationServiceImpl.canonicalizeUrl("http://3279880203/blah"));
    }

    @Test
    public void canonicalizeUrl_withPort_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://www.gotaport.com/",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.gotaport.com:1234/"));
    }

    @Test
    public void canonicalizeUrl_withLeadingSpaceInHexFormatInHostname_shouldBeOk()
            throws MalformedURLException {
        assertEquals(
                "http://%20leadingspace.com/",
                canonicalizationServiceImpl.canonicalizeUrl("http://%20leadingspace.com/"));
    }

    @Test
    public void canonicalizeUrl_withSchemeMissingAndLeadingSpaceInHexFormatInHostname_shouldBeOk()
            throws MalformedURLException {
        assertEquals(
                "http://%20leadingspace.com/",
                canonicalizationServiceImpl.canonicalizeUrl("%20leadingspace.com/"));
    }

    @Test
    public void canonicalizeUrl_withHexSymbols_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://%01%80.com/",
                canonicalizationServiceImpl.canonicalizeUrl("http://\\x01\\x80.com/"));
    }

    @Test
    public void canonicalizeUrl_withTwoSlashes_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://host.com/twoslashes?more//slashes",
                canonicalizationServiceImpl.canonicalizeUrl(
                        "http://host.com//twoslashes?more//slashes"));
    }

    @Test
    public void canonicalizeUrl_shouldBeOk() throws MalformedURLException {
        assertEquals(
                "http://www.google.com/",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.google.com/"));
        assertEquals(
                "http://www.google.com/q?",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.google.com/q?"));
        assertEquals(
                "http://www.google.com/q?r?",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.google.com/q?r?"));
        assertEquals(
                "http://www.google.com/q?r?s",
                canonicalizationServiceImpl.canonicalizeUrl("http://www.google.com/q?r?s"));
        assertEquals(
                "http://evil.com/foo;",
                canonicalizationServiceImpl.canonicalizeUrl("http://evil.com/foo;"));
        assertEquals(
                "http://evil.com/foo?bar;",
                canonicalizationServiceImpl.canonicalizeUrl("http://evil.com/foo?bar;"));
        assertEquals(
                "https://www.securesite.com/",
                canonicalizationServiceImpl.canonicalizeUrl("https://www.securesite.com/"));
        assertEquals(
                "http://host.com/ab%23cd",
                canonicalizationServiceImpl.canonicalizeUrl("http://host.com/ab%23cd"));
        assertEquals(
                "http://195.127.0.11/uploads/%20%20%20%20/.verify/.eBaysecure=updateuserdataxplimnbqmn-xplmvalidateinfoswqpcmlx=hgplmcx/",
                canonicalizationServiceImpl.canonicalizeUrl(
                        "http://195.127.0.11/uploads/%20%20%20%20/.verify/.eBaysecure=updateuserdataxplimnbqmn-xplmvalidateinfoswqpcmlx=hgplmcx/"));
        assertEquals(
                "http://host/asdf%25asd",
                canonicalizationServiceImpl.canonicalizeUrl("http://host/asdf%25%32%35asd"));
        assertEquals(
                "http://host/%25%25%25asd%25%25",
                canonicalizationServiceImpl.canonicalizeUrl("http://host/%%%25%32%35asd%%"));
        assertEquals(
                "http://zerod.me/%CE%B5%CE%BB%CE%BB%CE%AC%CF%82",
                canonicalizationServiceImpl.canonicalizeUrl("  http://zerod.me/ελλάς"));
    }

    @Test
    public void canonicalizeUrl_withEmptyUrl_shouldReturnNull() throws MalformedURLException {
        assertNull(canonicalizationServiceImpl.canonicalizeUrl(""));
    }

    @Test
    public void canonicalizeUrl_withMalformedUrlWithUnicodeCharacters_shouldThrowMalformedURLException() {
        assertThrows(
                MalformedURLException.class,
                () -> canonicalizationServiceImpl.canonicalizeUrl("http://ελλάς;\\;;"));
    }
}
