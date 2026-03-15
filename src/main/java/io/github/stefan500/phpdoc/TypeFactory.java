package io.github.stefan500.phpdoc;

import java.util.List;

public final class TypeFactory {
    private TypeFactory() {
    }

    public static PhpType mixed() {
        return new PhpType("mixed");
    }

    public static UnionType union(List<PhpType> members) {
        return new UnionType(members);
    }
}
