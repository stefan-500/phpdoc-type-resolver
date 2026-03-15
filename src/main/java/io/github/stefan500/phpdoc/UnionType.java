package io.github.stefan500.phpdoc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UnionType extends PhpType {
    private final List<PhpType> members;

    public UnionType(List<PhpType> members) {
        super(toUnionName(members));
        this.members = List.copyOf(Objects.requireNonNull(members, "members"));
    }

    public List<PhpType> getMembers() {
        return members;
    }

    private static String toUnionName(List<PhpType> members) {
        Objects.requireNonNull(members, "members");
        return members.stream().map(PhpType::getName).collect(Collectors.joining("|"));
    }
}
