package eu.mackatozis.hashing.urls.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UrlHashPrefixes implements Serializable {

    private static final long serialVersionUID = -7933479852501735419L;

    private String url;
    private Set<ExpressionHashes> expressionHashes;
}
