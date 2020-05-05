package eu.mackatozis.hashing.urls.service.impl;

import eu.mackatozis.hashing.urls.model.UrlComponents;
import eu.mackatozis.hashing.urls.util.IpUtils;
import eu.mackatozis.hashing.urls.util.UrlUtils;
import eu.mackatozis.hashing.urls.service.ExpressionExtractionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class deals with extracting expressions from a URL. Expressions are
 * all the possible host-suffix and path-prefix fragments for the input URL.
 * For example, the expressions for the given URL are the following:
 * <pre>
 * {@code
 * input: "http://a.b.c/1/2.html?param=1/2"
 * expression: [
 *     "a.b.c/1/2.html?param=1/2",
 *     "a.b.c/1/2.html",
 *     "a.b.c/1/",
 *     "a.b.c/",
 *     "b.c/1/2.html?param=1/2",
 *     "b.c/1/2.html",
 *     "b.c/1/",
 *     "b.c/"
 * ]
 * }
 * </pre>
 */
@Service
public class ExpressionExtractionServiceImpl implements ExpressionExtractionService {

    private static final int MAX_HOST_COMBINATIONS = 5;

    private static final int MAX_PATH_COMBINATIONS = 6;

    /**
     * Extracts all possible host-suffix and path-prefix expressions
     * for the input URL {@code String}.
     *
     * @param url URL {@code String} to extract expressions for
     * @return {@code Set} of all possible host-suffix and
     *     path-prefix expressions; an empty {@code Set} if expressions
     *     could not be extracted
     */
    @Override
    public Set<String> extractExpressions(String url) {
        UrlComponents urlComponents = UrlUtils.split(url);

        if (urlComponents == null) {
            return Collections.emptySet();
        }

        Set<String> urls = new HashSet<>();

        String host = urlComponents.getHost();
        String path = urlComponents.getPath();
        String query = urlComponents.getQuery();

        Set<String> hostSuffixExpressions = extractHostSuffixExpressions(host);
        Set<String> pathPrefixExpressions = extractPathPrefixExpressions(path, query);

        for (String hostSuffix : hostSuffixExpressions) {
            for (String pathPrefix : pathPrefixExpressions) {
                urls.add(hostSuffix + pathPrefix);
            }
        }

        return urls;
    }

    /**
     * Extracts all possible host-suffix expressions, which are at most five.
     * They are:
     *
     * <ul>
     *   <li>The exact hostname in the URL
     *   <li>Up to four hostnames formed by starting with the last five
     *       components and successively removing the leading component.
     *       The top-level domain can be skipped. These additional hostnames
     *       should not be checked if the host is an IP address
     * </ul>
     *
     * @param host host {@code String} to extract expressions for
     * @return {@code Set} of host-suffix expressions
     */
    private Set<String> extractHostSuffixExpressions(String host) {
        Set<String> suffixExpressions = new HashSet<>();
        suffixExpressions.add(host);

        if (!IpUtils.isIpAddress(host)) {
            String[] hostArray = host.split("\\.");
            StringBuilder builder = new StringBuilder();

            int start = (hostArray.length <= MAX_HOST_COMBINATIONS
                            ? 1
                            : hostArray.length - MAX_HOST_COMBINATIONS);
            int end = hostArray.length;

            for (int i = start; i < end - 1; i++) {
                builder.setLength(0);

                for (int j = i; j < end; j++) {
                    builder.append(hostArray[j]).append(".");
                }

                suffixExpressions.add(StringUtils.chop(builder.toString()));
            }
        }
        return suffixExpressions;
    }

    /**
     * Extracts all possible path-prefix expressions, which are at most six.
     * They are:
     *
     * <ul>
     *   <li>The exact path of the URL, including query parameters
     *   <li>The exact path of the URL, without query parameters
     *   <li>The four paths formed by starting at the root "(/)"
     *       and successively appending path components,
     *       including a trailing slash
     * </ul>
     *
     * @param path path {@code String} to extract expressions for
     * @param query query {@code String}
     * @return {@code Set} of path-prefix expressions
     */
    private Set<String> extractPathPrefixExpressions(String path, String query) {
        Set<String> prefixExpression = new HashSet<>();

        if (query != null) {
            prefixExpression.add(path + query);
        }
        prefixExpression.add(path);
        prefixExpression.add("/");

        int maxExpressions = (query == null ? MAX_PATH_COMBINATIONS - 1 : MAX_PATH_COMBINATIONS);

        StringBuilder builder = new StringBuilder();

        String[] pathArray = path.split("/");

        int upperBound = Math.min(pathArray.length, maxExpressions);

        for (int i = 0; i < upperBound; i++) {
            builder.append(pathArray[i]).append(!pathArray[i].contains(".") ? "/" : "");
            prefixExpression.add(builder.toString());
        }

        return prefixExpression;
    }
}
