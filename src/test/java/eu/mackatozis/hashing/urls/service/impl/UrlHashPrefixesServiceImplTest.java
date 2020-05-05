package eu.mackatozis.hashing.urls.service.impl;

import eu.mackatozis.hashing.urls.model.ExpressionHashes;
import eu.mackatozis.hashing.urls.model.UrlHashPrefixes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UrlHashPrefixesServiceImplTest {

    @Autowired
    private UrlHashPrefixesServiceImpl urlHashPrefixesServiceImpl;

    @Test
    public void findUrlHashPrefixes_shouldBeOk() throws MalformedURLException {
        UrlHashPrefixes actual =
                urlHashPrefixesServiceImpl.findUrlHashPrefixes("http://www.example.com/");

        assertEquals("http://www.example.com/", actual.getUrl());
        assertEquals(2, actual.getExpressionHashes().size());
        assertTrue(expectedExpressionHashes().containsAll(actual.getExpressionHashes()));
    }

    @Test
    public void findUrlHashPrefixes_withEmptyUrl_shouldThrowException() {
        assertThrows(
                MalformedURLException.class,
                () -> urlHashPrefixesServiceImpl.findUrlHashPrefixes(""));
    }

    @Test
    public void findUrlHashPrefixes_withMalformedUrl_shouldThrowException() {
        assertThrows(
                MalformedURLException.class,
                () -> urlHashPrefixesServiceImpl.findUrlHashPrefixes("http://example.com:-80/"));
    }

    private Set<ExpressionHashes> expectedExpressionHashes() {
        ExpressionHashes expressionHashesOne =
                ExpressionHashes.builder()
                        .expression("example.com/")
                        .fullHash(
                                "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2653f8a938cd801")
                        .hashPrefixes(
                                List.of(
                                        "73d986e0",
                                        "73d986e009",
                                        "73d986e00906",
                                        "73d986e009065f",
                                        "73d986e009065f18",
                                        "73d986e009065f182c",
                                        "73d986e009065f182c10",
                                        "73d986e009065f182c10bc",
                                        "73d986e009065f182c10bcb6",
                                        "73d986e009065f182c10bcb6a4",
                                        "73d986e009065f182c10bcb6a45d",
                                        "73d986e009065f182c10bcb6a45db3",
                                        "73d986e009065f182c10bcb6a45db3d6",
                                        "73d986e009065f182c10bcb6a45db3d6ed",
                                        "73d986e009065f182c10bcb6a45db3d6eda9",
                                        "73d986e009065f182c10bcb6a45db3d6eda949",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f89",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f893065",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930654a",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af265",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2653f",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2653f8a",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2653f8a93",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2653f8a938c",
                                        "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2653f8a938cd8"))
                        .build();

        ExpressionHashes expressionHashesTwo =
                ExpressionHashes.builder()
                        .expression("www.example.com/")
                        .fullHash(
                                "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5070fe145d87977")
                        .hashPrefixes(
                                List.of(
                                        "d59cc9d3",
                                        "d59cc9d3fe",
                                        "d59cc9d3fecd",
                                        "d59cc9d3fecd8c",
                                        "d59cc9d3fecd8cf9",
                                        "d59cc9d3fecd8cf920",
                                        "d59cc9d3fecd8cf920ea",
                                        "d59cc9d3fecd8cf920eadd",
                                        "d59cc9d3fecd8cf920eadd03",
                                        "d59cc9d3fecd8cf920eadd0301",
                                        "d59cc9d3fecd8cf920eadd03012f",
                                        "d59cc9d3fecd8cf920eadd03012f0b",
                                        "d59cc9d3fecd8cf920eadd03012f0be4",
                                        "d59cc9d3fecd8cf920eadd03012f0be497",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7e",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a507",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5070f",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5070fe1",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5070fe145",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5070fe145d8",
                                        "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5070fe145d879"))
                        .build();

        return Set.of(expressionHashesOne, expressionHashesTwo);
    }
}
