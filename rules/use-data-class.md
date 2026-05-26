---
alwaysApply: true
---

# Use Data Class

## Value Types Are `data class`

- Records, DTOs, request/response objects, domain values — these are `data class`
- A regular `class` with manually-written `equals`, `hashCode`, and `toString` is a `data class` that hasn't realised it yet
- The compiler-generated implementations are correct, fast, and impossible to forget to update when you add a field

## Never Hand-Roll `equals` / `hashCode` / `toString`

- If you find yourself overriding all three, the class is a data class — convert it
- Hand-rolled equality silently desyncs when a field is added later and the override isn't updated. This is one of the most common Kotlin bug factories in code ported from Java
- The only legitimate reason to override these on a data class is to mask a sensitive field in `toString` — in which case override only `toString`, not all three

## `copy()` Is the Mutation Pattern

- Update a `data class` instance via `instance.copy(field = newValue)` — preserves all other fields, gives you a new immutable instance
- This composes with `val` (the previous rule) — together they make immutable update painless
- Destructuring (`val (id, name) = user`) and `componentN()` come for free; lean on them when iterating collections of value objects

## The Public-API Exception

- This rule is about application and internal code. A value type on a **published library API** is the one place to think twice: the generated constructor, `copy()`, and `componentN()` signatures shift when you add or reorder a property, breaking binary compatibility for downstream consumers
- The [Kotlin Library Authors' guidelines](https://kotlinlang.org/docs/api-guidelines-backward-compatibility.html) advise a regular class for stable public API surface for exactly this reason
- When the type is library API, reach for the `kotlin-api-review` skill before converting
