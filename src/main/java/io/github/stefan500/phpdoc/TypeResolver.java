package io.github.stefan500.phpdoc;

import java.util.ArrayList;
import java.util.List;

public class TypeResolver {
    /**
     * Finds the most appropriate @var tag for the provided variable and resolves its type.
     */
    public PhpType inferTypeFromDoc(PhpVariable variable) {
        if (variable == null || variable.getDocBlock() == null || variable.getDocBlock().getTags().isEmpty()) {
            return TypeFactory.mixed();
        }

        PhpType fallbackType = null;

        for (DocTag tag : variable.getDocBlock().getTags()) {
            ParsedTagValue parsed = ParsedTagValue.parse(tag);
            if (!parsed.hasType()) {
                continue;
            }

            if (parsed.isNamedTag()) {
                if (parsed.variableName().equals(variable.getName())) {
                    return resolveType(parsed.typeExpression());
                }
                continue;
            }

            if (fallbackType == null) {
                fallbackType = resolveType(parsed.typeExpression());
            }
        }

        if (fallbackType != null) {
            return fallbackType;
        }

        return TypeFactory.mixed();
    }

    private PhpType resolveType(String rawType) {
        ParsedType parsedType = ParsedType.parse(rawType);
        if (!parsedType.hasMembers()) {
            return TypeFactory.mixed();
        }

        if (parsedType.members().size() == 1) {
            return parsedType.members().get(0);
        }

        return TypeFactory.union(parsedType.members());
    }

    private static record ParsedType(List<PhpType> members) {
        static ParsedType parse(String rawType) {
            String normalized = rawType.trim();
            if (normalized.isEmpty()) {
                return new ParsedType(List.of());
            }

            String[] parts = normalized.split("\\|");
            List<PhpType> members = new ArrayList<>();
            for (String part : parts) {
                String member = part.trim();
                if (member.isEmpty()) {
                    return new ParsedType(List.of());
                }
                members.add(new PhpType(member));
            }
            return new ParsedType(List.copyOf(members));
        }

        boolean hasMembers() {
            return !members.isEmpty();
        }
    }

    private static record ParsedTagValue(String typeExpression, String variableName) {
        static ParsedTagValue parse(DocTag tag) {
            if (tag == null) {
                return new ParsedTagValue("", null);
            }
            return splitValueAndVariable(tag.getValue(), tag.getVariableName());
        }

        static ParsedTagValue splitValueAndVariable(String rawValue, String explicitVariableName) {
            if (rawValue == null) {
                return new ParsedTagValue("", null);
            }

            String value = rawValue.trim();
            if (value.isEmpty()) {
                return new ParsedTagValue("", null);
            }

            String variableName = explicitVariableName == null ? null : explicitVariableName.trim();
            String typeExpression = value;

            if (variableName != null && !variableName.isBlank()) {
                int inlineIndex = value.lastIndexOf(variableName);
                if (inlineIndex > 0 && inlineIndex + variableName.length() == value.length()) {
                    typeExpression = value.substring(0, inlineIndex).trim();
                }
                return new ParsedTagValue(typeExpression, variableName);
            }

            String lastToken = lastWhitespaceSeparatedToken(value);
            if (isVariableToken(lastToken)) {
                int tokenIndex = value.lastIndexOf(lastToken);
                typeExpression = value.substring(0, tokenIndex).trim();
                variableName = lastToken;
            }

            return new ParsedTagValue(typeExpression, variableName);
        }

        private static String lastWhitespaceSeparatedToken(String value) {
            String[] tokens = value.split("\\s+");
            if (tokens.length < 2) {
                return null;
            }
            return tokens[tokens.length - 1];
        }

        private static boolean isVariableToken(String token) {
            return token != null && token.matches("\\$[A-Za-z_][A-Za-z0-9_]*");
        }

        boolean hasType() {
            return !typeExpression.isBlank();
        }

        boolean isNamedTag() {
            return variableName != null && !variableName.isBlank();
        }
    }
}
