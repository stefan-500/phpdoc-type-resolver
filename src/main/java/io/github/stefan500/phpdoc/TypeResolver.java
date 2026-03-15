package io.github.stefan500.phpdoc;

public class TypeResolver {
    public PhpType inferTypeFromDoc(PhpVariable variable) {
        return TypeFactory.mixed();
    }
}
