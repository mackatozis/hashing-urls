package eu.mackatozis.hashing.urls.controller;

import eu.mackatozis.hashing.urls.model.UrlHashPrefixes;
import eu.mackatozis.hashing.urls.service.UrlHashPrefixesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

/**
 * <pre>
 * Rest Controller that serves a single GET endpoint:
 *
 * {@code /url-hash-prefixes}
 *
 * for finding all hash prefixes of the input URL {@code String}
 * which is passed as a request parameter.
 * </pre>
 */
@RestController
@RequestMapping("hash-prefixes")
@RequiredArgsConstructor
public class UrlHashPrefixesController {

    private final UrlHashPrefixesService urlHashPrefixesService;

    /**
     * Finds all hash prefixes of the input URL {@code String}.
     *
     * @param url required URL {@code String} to find hash prefixes for
     * @return {@code HttpStatus.OK} with the hash prefixes of the input
     * URL {@code String}; {@code HttpStatus.BAD_REQUEST} if the
     * URL {@code String} is a Malformed URL or the {@code "url"}
     * request param is missing.
     */
    @GetMapping
    public ResponseEntity<UrlHashPrefixes> findUrlHashPrefixes(
            @RequestParam(value = "url") String url) {
        try {
            return new ResponseEntity<>(
                    urlHashPrefixesService.findUrlHashPrefixes(url), HttpStatus.OK);
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
