package io.github.stefan500.phpdoc;

import java.util.Objects;

public class DocTag {
    private final String typeExpression;
    private final String variableName;

    public DocTag(String typeExpression, String variableName) {
        this.typeExpression = Objects.requireNonNull(typeExpression, "typeExpression");
        this.variableName = variableName;
    }

    public String getTypeExpression() {
        return typeExpression;
    }

    public String getVariableName() {
        return variableName;
    }
}
