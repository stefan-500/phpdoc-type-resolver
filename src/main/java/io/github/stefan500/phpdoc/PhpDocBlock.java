package io.github.stefan500.phpdoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PhpDocBlock {
    private final List<DocTag> tags;

    public PhpDocBlock(List<DocTag> tags) {
        this.tags = List.copyOf(Objects.requireNonNull(tags, "tags"));
    }

    public List<DocTag> getTags() {
        return tags;
    }

    public static PhpDocBlock empty() {
        return new PhpDocBlock(Collections.emptyList());
    }

    public static PhpDocBlock fromTags(DocTag... tags) {
        List<DocTag> values = new ArrayList<>();
        Collections.addAll(values, tags);
        return new PhpDocBlock(values);
    }
}
