package eu.mackatozis.hashing.urls.service.impl;

import com.google.common.base.CharMatcher;
import eu.mackatozis.hashing.urls.model.NormalizedUrl;
import eu.mackatozis.hashing.urls.model.UrlComponents;
import eu.mackatozis.hashing.urls.service.CanonicalizationService;
import eu.mackatozis.hashing.urls.util.IpUtils;
import eu.mackatozis.hashing.urls.util.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * This class deals with parsing a URL and return it as:
 *
 * {@code scheme://hostname/path}
 *
 * It strips off fragments and queries.
 * </pre>
 */
@Service
public class CanonicalizationServiceImpl implements CanonicalizationService {

    private static final Pattern HTTP_SCHEME_PATTERN =
            Pattern.compile("^http[s]?://", Pattern.CASE_INSENSITIVE);

    private static final Pattern PERCENT_SYMBOL_PATTERN =
            Pattern.compile("%[0-9a-f]{2}", Pattern.CASE_INSENSITIVE);

    private static final Pattern HEX_SYMBOL_PATTERN =
            Pattern.compile("\\\\x([0-9a-f]+)", Pattern.CASE_INSENSITIVE);

    /**
     * Canonicalizes the input URL {@code String}.
     *
     * @param url URL {@code String} to canonicalize
     * @return canonicalized URL {@code String} if canonicalized successfully;
     * {@code null} otherwise
     * @throws MalformedURLException when supplied URL {@code String} is a malformed URL
     */
    @Override
    public String canonicalizeUrl(String url) throws MalformedURLException {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        NormalizedUrl normalizedUrl = normalizeUrl(url);

        String host = canonicalizeHost(normalizedUrl);
        String path = canonicalizePath(normalizedUrl);

        url = normalizedUrl
                        .getUrl()
                        .replace(normalizedUrl.getHost(), host)
                        .replace(normalizedUrl.getPath(), path);

        StringBuilder builder = new StringBuilder();
        for (char ch : url.toCharArray()) {
            if (ch <= 32 || ch >= 127 || ch == '#' || ch == '%') {
                builder.append(UrlUtils.encode(Character.toString(ch)).toUpperCase());
            } else {
                builder.append(ch);
            }
        }

        return convertSlashHexSymbols(builder.toString());
    }

    /**
     * Normalizes the input URL {@code String} by:
     *
     * <ul>
     *   <li>Removing leading/ending white spaces
     *   <li>Making sure the url has a scheme
     *   <li>Converting host part to ASCII Punycode representation if needed
     *   <li>Removing any embedded tabs, CR/LF characters which aren't escaped
     *   <li>Removing fragment part
     *   <li>Removing percent-escapes from the URL
     *   <li>Removing port number if exists
     *   <li>Ensuring that path ends with a slash
     * </ul>
     *
     * @param url URL {@code String} to canonicalize
     * @return normalized URL {@code String}
     * @throws MalformedURLException when supplied URL {@code String} is a malformed URL
     */
    private NormalizedUrl normalizeUrl(String url) throws MalformedURLException {
        url = url.trim();

        if (!HTTP_SCHEME_PATTERN.matcher(url).find()) {
            url = "http://" + url;
        }

        boolean isAscii = CharMatcher.ascii().matchesAllOf(url);

        if (!isAscii) {
            try {
                URI tmp = new URI(url);
                String authorityPunyCode = IDN.toASCII(tmp.getAuthority());
                url = url.replace(tmp.getAuthority(), authorityPunyCode);
            } catch (URISyntaxException e) {
                throw new MalformedURLException(
                        "Could not convert host part to ASCII Punycode representation of url "
                                + url);
            }
        }

        url = url.replaceAll("[\t\r\n]", "");

        int fragmentIndex = url.indexOf('#');
        if (fragmentIndex != -1) {
            url = url.substring(0, fragmentIndex);
        }

        url = stripPercentEscapes(url);

        UrlComponents urlComponents = UrlUtils.split(url);

        if (urlComponents != null) {
            if (StringUtils.isNotBlank(urlComponents.getPort())) {
                url = url.replaceFirst(urlComponents.getPort(), "");
            }

            String path;

            if (urlComponents.getPath() == null) {
                path = "/";

                if (urlComponents.getQuery() == null) {
                    url = url + "/";
                }
            } else {
                path = urlComponents.getPath();
            }

            return NormalizedUrl.builder()
                    .url(url)
                    .host(urlComponents.getHost())
                    .path(path)
                    .build();
        } else {
            throw new MalformedURLException("Could not normalize url " + url);
        }
    }

    /**
     * Repeatedly removes percent-escapes from the URL until it has no more percent-escapes
     *
     * @param url URL {@code String} to process
     * @return percent-escaped free URL @code String}
     */
    private String stripPercentEscapes(String url) {
        while (PERCENT_SYMBOL_PATTERN.matcher(url).find()) {
            StringBuffer buffer = new StringBuffer();
            StringBuilder builder = new StringBuilder();
            Matcher matcher = PERCENT_SYMBOL_PATTERN.matcher(url);
            String decoded;

            while (matcher.find()) {
                decoded = UrlUtils.decode(matcher.group());

                if (decoded.equals("$") || decoded.equals("\\")) {
                    decoded = builder.append("\\").append(decoded).toString();
                }
                matcher.appendReplacement(buffer, decoded);
            }
            matcher.appendTail(buffer);
            url = buffer.toString();
        }
        return url;
    }

    /**
     * Canonicalizes the host part of the supplied {@code NormalizedUrl} by:
     *
     * <ul>
     *   <li>Removing all leading and trailing dots
     *   <li>Replacing consecutive dots with a single dot
     *   <li>If the hostname can be parsed as an IP address, it normalizes to 4 dot-separated
     *       decimal values
     *   <li>Handling any legal IP-address encoding, including octal, hex, and fewer than four
     *       components
     *   <li>Lowercasing the whole {@code String}
     * </ul>
     *
     * @param normalizedUrl The {@code NormalizedUrl} to process
     * @return URL's host {@code String} canonicalized
     */
    private String canonicalizeHost(NormalizedUrl normalizedUrl) {
        String host = normalizedUrl.getHost().toLowerCase();

        host = host.replaceFirst("^[.]+", "")
                        .replaceFirst("[.]+$", "")
                        .replaceAll("(\\.)+\\1+", ".");

        if (IpUtils.isIpAddress(host) && !IpUtils.isDecimalIpAddress(host)) {
            String ipAddress = IpUtils.resolveDecimalIpAddress(host);
            if (StringUtils.isNotBlank(ipAddress)) {
                host = ipAddress;
            }
        }
        return host;
    }

    /**
     * Canonicalizes the path part of the supplied {@code NormalizedUrl} by:
     *
     * <ul>
     *   <li>Resolving the sequences /../ and /./ in the path by replacing /./ with /
     *   <li>Removing /../ along with the preceding path component
     *   <li>Replacing consecutive slashes with a single slash character
     * </ul>
     *
     * <p>NOTE: It does not apply these path canonicalizations to the query parameters.
     *
     * @param normalizedUrl The {@code NormalizedUrl} to process
     * @return URL's path {@code String} canonicalized
     */
    private String canonicalizePath(NormalizedUrl normalizedUrl) {
        String path = normalizedUrl.getPath();

        path = path.replaceAll("/\\./", "/")
                        .replaceAll("/\\w+/\\.\\./?", "/")
                        .replaceAll("(/)+\\1+", "/");

        return path;
    }

    /**
     * Converts \xYY to %YY otherwise hex symbols like %YY would become %25YY.
     *
     * @param input input {@code String} to process
     * @return processed {@code String}
     */
    private String convertSlashHexSymbols(String input) {
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = HEX_SYMBOL_PATTERN.matcher(input);

        while (matcher.find()) {
            matcher.appendReplacement(buffer, "%" + matcher.group(1));
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
