package eu.mackatozis.hashing.urls.util;

import eu.mackatozis.hashing.urls.model.UrlComponents;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UrlUtilsTest {

    @Test
    public void split_shouldBeOk() {
        UrlComponents urlComponents =
                UrlUtils.split("http://www.example.com:80/some/path?queryParam=1");

        assertNotNull(urlComponents);
        assertEquals("http://", urlComponents.getScheme());
        assertEquals("www.example.com", urlComponents.getHost());
        assertEquals(":80", urlComponents.getPort());
        assertEquals("/some/path", urlComponents.getPath());
        assertEquals("?queryParam=1", urlComponents.getQuery());
    }

    @Test
    public void split_withIpAddress_shouldBeOk() {
        UrlComponents urlComponents =
                UrlUtils.split("http://192.168.0.1:80/some/path?queryParam=1");

        assertNotNull(urlComponents);
        assertEquals("http://", urlComponents.getScheme());
        assertEquals("192.168.0.1", urlComponents.getHost());
        assertEquals(":80", urlComponents.getPort());
        assertEquals("/some/path", urlComponents.getPath());
        assertEquals("?queryParam=1", urlComponents.getQuery());
    }

    @Test
    public void split_withPortAndPathMissing_shouldBeOk() {
        UrlComponents urlComponents = UrlUtils.split("http://www.example.com?queryParam=1");

        assertNotNull(urlComponents);
        assertEquals("http://", urlComponents.getScheme());
        assertEquals("www.example.com", urlComponents.getHost());
        assertNull(urlComponents.getPort());
        assertNull(urlComponents.getPath());
        assertEquals("?queryParam=1", urlComponents.getQuery());
    }

    @Test
    public void split_withPortAndQueryMissing_shouldBeOk() {
        UrlComponents urlComponents = UrlUtils.split("http://www.example.com/path/1/");

        assertNotNull(urlComponents);
        assertEquals("http://", urlComponents.getScheme());
        assertEquals("www.example.com", urlComponents.getHost());
        assertNull(urlComponents.getPort());
        assertEquals("/path/1/", urlComponents.getPath());
        assertNull(urlComponents.getQuery());
    }

    @Test
    public void split_withPortAndPathAndQueryMissing_shouldBeOk() {
        UrlComponents urlComponents = UrlUtils.split("http://www.example.com");

        assertNotNull(urlComponents);
        assertEquals("http://", urlComponents.getScheme());
        assertEquals("www.example.com", urlComponents.getHost());
        assertNull(urlComponents.getPort());
        assertNull(urlComponents.getPath());
        assertNull(urlComponents.getQuery());
    }

    @Test
    public void split_withRandomText_shouldReturnNull() {
        assertNull(UrlUtils.split(RandomStringUtils.random(10, true, false)));
    }

    @Test
    public void split_withEmptyString_shouldReturnNull() {
        assertNull(UrlUtils.split(""));
    }

    @Test
    public void encode_shouldBeOk() {
        assertEquals(
                "query%3Dpanthera%20tigris%26count%3D10",
                UrlUtils.encode("query=panthera tigris&count=10"));
        assertEquals(
                "query%3D%22panthera%22%20%22tigris%22",
                UrlUtils.encode("query=\"panthera\" \"tigris\""));
        assertEquals("%25%25%2525%2532%2535asd%25%25", UrlUtils.encode("%%%25%32%35asd%%"));
    }

    @Test
    public void decode_shouldBeOk() {
        assertEquals(
                "query=panthera tigris&count=10",
                UrlUtils.decode("query%3Dpanthera%20tigris%26count%3D10"));
        assertEquals(
                "query=\"panthera\" \"tigris\"",
                UrlUtils.decode("query%3D%22panthera%22%20%22tigris%22"));
        assertEquals("%%%25%32%35asd%%", UrlUtils.decode("%25%25%2525%2532%2535asd%25%25"));
    }
}
