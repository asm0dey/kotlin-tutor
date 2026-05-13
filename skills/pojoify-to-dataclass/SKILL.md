---
name: pojoify-to-dataclass
description: >
  Refactor a regular Kotlin `class` with hand-written `equals`, `hashCode`,
  `toString`, and Java-bean accessors (getX/setX) into an idiomatic Kotlin
  `data class` with `val` properties, nullable `?` types, and compiler-
  generated equality. Removes the boilerplate that ports from Java leave
  behind. Use when the user asks to "make this a data class," "convert this
  POJO," "kotlinify this model," or shows a class with all three of equals /
  hashCode / toString manually overridden.
---

# Pojoify To Data Class

Process steps in order. Do not skip ahead.

## Step 1 — Verify The Class Is A Value Type

A class is a candidate when it has all of:

- Fields that look like data (no behaviour beyond getters and setters)
- Manually-overridden `equals`, `hashCode`, AND `toString` — all three
- No inheritance from a non-data class with state

If any of these are missing, stop and report what you found. Not every class with `equals`/`hashCode` is a data class candidate — some have legitimate inheritance or behaviour.

## Step 2 — Capture The Field Order

- Read the existing class and list the fields in the order they appear
- The `data class` primary constructor parameters MUST appear in the same order — otherwise existing `componentN()` destructuring breaks for callers
- If the existing class has both public fields and Java-bean accessors, the accessor naming tells you the canonical field name (`getCustomerId` → `customerId`)

## Step 3 — Rewrite The Class Declaration

- Change `class Foo { val a: String; val b: Int; … }` to `data class Foo(val a: String, val b: Int)`
- Convert any `var` to `val` UNLESS the field has a documented reason to mutate (rare; flag any retained `var` in the conversion summary)
- Move computed properties (`val derived: T get() = …`) into the class body, after the primary constructor
- If a field had a custom getter that did real work (validation, lazy init), preserve it in the class body, but flag for human review — data class generated equality reads the constructor-declared property

## Step 4 — Remove The Manually-Overridden Methods

- Delete the body of the class's `equals`, `hashCode`, `toString` — the compiler generates them now
- If `toString` was overridden to MASK a sensitive field (passwords, tokens), keep ONLY `toString` as a manual override — delete `equals` and `hashCode`, those are safe to auto-generate
- Remove `init` blocks that only validated equality contract assumptions; the compiler handles that
- Remove Java-bean getX / setX methods if they exist — Kotlin generates them from `val` / `var` properties

## Step 5 — Update Callers If Needed

- Glob the codebase for callers using setX methods on this type — those won't compile anymore
- Replace `foo.setX(value)` with `foo.copy(x = value)` — this is the idiomatic mutation pattern (see rule `use-data-class`)
- Replace `Foo(a, b)` callers that relied on positional construction — they keep working IF Step 2 preserved field order

## Step 6 — Run The Tests

- Run the project's test suite
- If any test fails, the conversion broke a contract — common causes: field order change, removed validation logic, caller using setter
- Fix and re-run; do not declare complete with red tests

Finish here. Do not commit — that's the operator's call.
