package io.github.stefan500.phpdoc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeResolverTest {
    @Test
    void returnsStandardTypeForSimpleVarTag() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = variable("$user", docTag("User", null));

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(new PhpType("User"), inferred);
    }

    @Test
    void returnsUnionTypeForPipeSeparatedTag() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = variable("$value", docTag("string|int", null));

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(new PhpType("string|int"), inferred);
    }

    @Test
    void returnsNamedTagTypeWhenNameMatches() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = variable(
            "$log",
            docTag("Logger", "$log"),
            docTag("User", null)
        );

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(new PhpType("Logger"), inferred);
    }

    @Test
    void returnsMixedWhenNamedTagVariableDoesNotMatch() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = variable("$guest", docTag("Admin", "$adm"));

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(TypeFactory.mixed(), inferred);
    }

    @Test
    void choosesTheTagThatMatchesTheInspectedVariableWhenMultipleTagsArePresent() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = variable(
            "$session",
            docTag("Admin", "$admin"),
            docTag("Session", "$session"),
            docTag("User", "$user")
        );

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(new PhpType("Session"), inferred);
    }

    @Test
    void fallsBackToMixedWhenNoDocBlockIsProvided() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable("$value");

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(TypeFactory.mixed(), inferred);
    }

    @Test
    void fallsBackToMixedWhenDocBlockHasNoVarTags() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable("$value", PhpDocBlock.empty());

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(TypeFactory.mixed(), inferred);
    }

    @Test
    void handlesExtraWhitespaceInTagValues() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = variable("$user", docTag("  User  ", "$user"));

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(new PhpType("User"), inferred);
    }

    @Test
    void handlesUnionTypeWithWhitespaceAroundSeparator() {
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = variable("$value", docTag("string | int", null));

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(new PhpType("string|int"), inferred);
    }

    @Test
    void resolvesToMixedForBlankOrEmptyTagValues() {
        TypeResolver resolver = new TypeResolver();
        List<DocTag> tags = Arrays.asList(
            docTag("", null),
            docTag("   ", null)
        );
        PhpVariable variable = variable("$value", new PhpDocBlock(tags));

        PhpType inferred = resolver.inferTypeFromDoc(variable);

        assertEquals(TypeFactory.mixed(), inferred);
    }

    private static PhpVariable variable(String name, DocTag... tags) {
        return new PhpVariable(name, new PhpDocBlock(Arrays.asList(tags)));
    }

    private static DocTag docTag(String rawValue, String variableName) {
        return new DocTag(rawValue, variableName);
    }

    private static PhpVariable variable(String name, PhpDocBlock docBlock) {
        return new PhpVariable(name, docBlock);
    }
}
