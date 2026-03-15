package io.github.stefan500.phpdoc;

import java.util.Objects;

public class PhpType {
    private final String name;

    public PhpType(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhpType phpType)) {
            return false;
        }
        return name.equals(phpType.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
