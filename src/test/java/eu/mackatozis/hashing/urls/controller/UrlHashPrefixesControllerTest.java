package eu.mackatozis.hashing.urls.controller;

import eu.mackatozis.hashing.urls.service.UrlHashPrefixesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.MalformedURLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UrlHashPrefixesControllerTest {

    @Autowired
    private UrlHashPrefixesController urlHashPrefixesController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlHashPrefixesService urlHashPrefixesService;

    @Test
    public void findUrlHashPrefixes_shouldBeOk() throws Exception {
        mockMvc.perform(get("/hash-prefixes")
                            .queryParam("url", "http://www.example.com/"))
                        .andExpect(status().isOk());

        verify(urlHashPrefixesService, times(1)).findUrlHashPrefixes(anyString());
    }

    @Test
    public void findUrlHashPrefixes_withEmptyUrl_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/hash-prefixes"))
                .andExpect(status().isBadRequest());

        verify(urlHashPrefixesService, never()).findUrlHashPrefixes(anyString());
    }

    @Test
    public void findUrlHashPrefixes_serviceThrowsMalformedURLException_shouldReturnBadRequest() throws Exception {
        given(urlHashPrefixesService.findUrlHashPrefixes(anyString()))
                .willThrow(MalformedURLException.class);

        mockMvc.perform(get("/hash-prefixes")
                            .queryParam("url", "http://www.example.com/"))
                        .andExpect(status().isBadRequest());

        verify(urlHashPrefixesService, times(1)).findUrlHashPrefixes(anyString());
    }
}
