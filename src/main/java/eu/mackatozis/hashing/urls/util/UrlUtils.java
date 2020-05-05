package eu.mackatozis.hashing.urls.util;

import eu.mackatozis.hashing.urls.model.UrlComponents;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlUtils {

    private static final Pattern URL_PATTERN =
            Pattern.compile("(https?://)([^/:?]+)(:[0-9]+)?(/[^?]*)?(\\?.*)?");

    /**
     * Split input URL {@code String} into host, port, path and query.
     *
     * @param url URL {@code String} to process
     * @return {@code UrlComponents} with all the components of the url;
     *         {@code null} if input URL {@code String} is empty
     */
    public static UrlComponents split(String url) {
        if (StringUtils.isNotBlank(url)) {
            Matcher matcher = URL_PATTERN.matcher(url.trim());
            if (matcher.matches()) {
                return UrlComponents.builder()
                        .scheme(matcher.group(1))
                        .host(matcher.group(2))
                        .port(matcher.group(3))
                        .path(matcher.group(4))
                        .query(matcher.group(5))
                        .build();
            }
        }
        return null;
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public static String encode(String url) {
        String urlEncoded = URLEncoder.encode(url, StandardCharsets.UTF_8.name());
        return urlEncoded.replace("+", "%20");
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public static String decode(String encodedUrl) {
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.name());
    }
}
