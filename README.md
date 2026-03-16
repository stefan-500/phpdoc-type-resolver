# PHP Variable Type Resolver

Small Java 21 utility that infers the type of a PHP variable from its `@var` PHPDoc annotation.

## What it does

The project implements a resolver for:
```java
public PhpType inferTypeFromDoc(PhpVariable variable)
```

It inspects a variable’s doc block, reads its @var tags, and determines which tag applies to the variable being analyzed.


Supported cases:

- standard types, e.g. `@var User`
- union types, e.g. `@var string|int`
- named tags, e.g. `@var Logger $log`
- multiple `@var` tags in the same doc block
- fallback to `mixed` if no matching tag is found

## API overview

### `PhpVariable`
Represents the inspected PHP variable.

- `getDocBlock()` — returns the attached `PhpDocBlock`, or `null`
- `getName()` — returns the variable name, for example `"$user"`

### `PhpDocBlock`
Represents a PHPDoc block.

- `getTagsByName(String tagName)` — returns all tags with the given name

### `DocTag`
Represents a single doc tag.

- `getValue()` — returns the raw tag value, for example `"User $admin"`

### `TypeFactory`
Creates result types.

- `createType(String typeName)` — creates a `PhpType`
- `createUnionType(List<PhpType> types)` — creates a union type

## How resolution works

The resolver:

1. reads all `@var` tags from the variable’s doc block
2. checks whether a tag explicitly names a variable
3. prefers the tag whose variable name exactly matches the inspected variable
4. falls back to an unnamed `@var` tag when needed
5. returns `mixed` if there are no matching tags

## Commands
To run the build and tests, execute the following command in the project directory:
```bash
./gradlew build
```
