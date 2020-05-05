package eu.mackatozis.hashing.urls.service.impl;

import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsIterableContaining;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
public class ExpressionExtractionServiceImplTest {

    @Autowired
    private ExpressionExtractionServiceImpl expressionExtractionServiceImpl;

    @Test
    public void resolveExpressions_scenario1_shouldBeOk() {
        Set<String> actual =
                expressionExtractionServiceImpl.extractExpressions("http://a.b.c/1/2.html?param=1");

        assertThat(actual, hasSize(8));
        assertThat(
                actual,
                IsIterableContaining.hasItems(
                        "a.b.c/1/2.html?param=1",
                        "a.b.c/1/2.html",
                        "a.b.c/",
                        "a.b.c/1/",
                        "b.c/1/2.html?param=1",
                        "b.c/1/2.html",
                        "b.c/",
                        "b.c/1/"));
    }

    @Test
    public void resolveExpressions_scenario2_shouldBeOk() {
        Set<String> actual =
                expressionExtractionServiceImpl.extractExpressions("http://a.b.c.d.e.f.g/1.html");

        assertThat(actual, hasSize(10));
        assertThat(
                actual,
                IsIterableContaining.hasItems(
                        "a.b.c.d.e.f.g/1.html",
                        "a.b.c.d.e.f.g/",
                        "c.d.e.f.g/1.html",
                        "c.d.e.f.g/",
                        "d.e.f.g/1.html",
                        "d.e.f.g/",
                        "e.f.g/1.html",
                        "e.f.g/",
                        "f.g/1.html",
                        "f.g/"));
    }

    @Test
    public void resolveExpressions_scenario3_shouldBeOk() {
        Set<String> actual =
                expressionExtractionServiceImpl.extractExpressions("http://1.2.3.4/1/");

        assertThat(actual, hasSize(2));
        assertThat(actual, IsIterableContaining.hasItems("1.2.3.4/1/", "1.2.3.4/"));
    }

    @Test
    public void resolveExpressions_withEmptyUrl_shouldReturnEmptyCollection() {
        assertThat(
                expressionExtractionServiceImpl.extractExpressions(""), IsEmptyCollection.empty());
    }
}
