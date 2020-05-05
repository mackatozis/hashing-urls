package eu.mackatozis.hashing.urls.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UrlComponents implements Serializable {

    private static final long serialVersionUID = 7589833750958350125L;

    private String scheme;
    private String host;
    private String port;
    private String path;
    private String query;
}
