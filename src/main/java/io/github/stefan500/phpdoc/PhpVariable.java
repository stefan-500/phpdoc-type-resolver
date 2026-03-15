package io.github.stefan500.phpdoc;

import java.util.Objects;

public class PhpVariable {
    private final String name;
    private final PhpDocBlock docBlock;

    public PhpVariable(String name, PhpDocBlock docBlock) {
        this.name = Objects.requireNonNull(name, "name");
        this.docBlock = Objects.requireNonNullElseGet(docBlock, PhpDocBlock::empty);
    }

    public PhpVariable(String name) {
        this(name, PhpDocBlock.empty());
    }

    public String getName() {
        return name;
    }

    public PhpDocBlock getDocBlock() {
        return docBlock;
    }
}
