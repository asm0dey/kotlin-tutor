# jbaruch/kotlin-tutor

[![tessl](https://img.shields.io/endpoint?url=https%3A%2F%2Fapi.tessl.io%2Fv1%2Fbadges%2Fjbaruch%2Fkotlin-tutor)](https://tessl.io/registry/jbaruch/kotlin-tutor)

Teaches AI coding agents to write idiomatic Kotlin instead of Java-in-a-`.kt`-file. Six rules covering the highest-leverage Kotlin idioms, three skills that perform the common conversions, and a CI script that gates the JUnit-to-Kotest migration deterministically.

## Why This Plugin Exists

A coding agent without Kotlin context writes Kotlin like a Java developer who learned the syntax over a long weekend: regular `class` instead of `data class`, manual `equals` / `hashCode`, `var` properties everywhere, `Optional<String>` for nullables, JUnit assertions in test files. It compiles. It runs. It's Java in a `.kt` file. This plugin closes the gap — one `tessl install` and your agent knows the difference.

## Installation

```
tessl install jbaruch/kotlin-tutor
```

## What's Included

### Rules (6)

| ID | Rule | Summary |
|----|------|---------|
| K-1 | [prefer-val](rules/prefer-val.md) | Properties default to `val`; `var` is opt-in for mutation |
| K-2 | [nullable-question-mark](rules/nullable-question-mark.md) | `String?` not `Optional<String>`; lean on `?.` and `?:` |
| K-3 | [use-data-class](rules/use-data-class.md) | Value types are `data class`; never hand-roll equals/hashCode/toString |
| K-4 | [kotest-over-junit](rules/kotest-over-junit.md) | Tests use Kotest matchers and specs, not `assertEquals` and `@Test` |
| K-5 | [prefer-stdlib-scope](rules/prefer-stdlib-scope.md) | Use `.let` / `.also` / `.apply` over imperative Java patterns |
| K-6 | [extension-over-util](rules/extension-over-util.md) | Extension functions, not `*Utils` classes with static methods |

### Skills (3)

| Skill | Description |
|-------|-------------|
| [kotlinify-tests](skills/kotlinify-tests/SKILL.md) | Convert JUnit-style test classes to idiomatic Kotest specs; delegates to `verify-no-junit-assertions.sh` for the deterministic gate |
| [pojoify-to-dataclass](skills/pojoify-to-dataclass/SKILL.md) | Refactor a Java-style POJO (manual equals/hashCode/toString, bean accessors) into an idiomatic Kotlin `data class` |
| [nullable-cleanup](skills/nullable-cleanup/SKILL.md) | Replace `Optional<T>` with `T?` and the `?.` / `?:` / `?.let { }` operators |

### Scripts (1)

| Script | Purpose |
|--------|---------|
| [verify-no-junit-assertions.sh](scripts/verify-no-junit-assertions.sh) | Fails CI if any test file under `src/test/` still uses JUnit-style assertions. Delegated from the `kotlinify-tests` skill; runs standalone as a CI gate. Enforces rule K-4. |

## The Skill / Script Delegation Pattern

The `kotlinify-tests` skill demonstrates the script-delegation pattern in miniature: the skill does the judgment work (which Kotest matcher fits this JUnit call? what spec style preserves the original structure?), and `verify-no-junit-assertions.sh` does the deterministic gate (did any JUnit assertion slip through?). Judgment lives in the skill; mechanism lives in the shell. The script also runs when the agent isn't present — human commits go through the same gate in CI.

## Conventions

- All six rules apply per-turn (frontmatter `alwaysApply: true`); agents see them on every prompt
- Skills are intent-discovered via the trigger phrases in their `description` field
- Rule IDs (`K-1` through `K-6`) are talk-shorthand for the descriptive filenames; the registry uses the filenames

See [CHANGELOG.md](CHANGELOG.md) for version history.
