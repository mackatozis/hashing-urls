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
public class NormalizedUrl implements Serializable {

    private static final long serialVersionUID = 5586247594832685577L;

    private String url;
    private String host;
    private String path;
}
