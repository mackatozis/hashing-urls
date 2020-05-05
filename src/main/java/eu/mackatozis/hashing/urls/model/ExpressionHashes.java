package eu.mackatozis.hashing.urls.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExpressionHashes implements Serializable {

    private static final long serialVersionUID = 6190943517818522746L;

    private String expression;
    private String fullHash;
    private List<String> hashPrefixes;
}
