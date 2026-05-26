---
name: kotlin-api-review
description: >
  Review a Kotlin library's public API surface for the concerns that only
  matter once an API is published: simplicity, readability, consistency,
  predictability, debuggability, testability, backward compatibility,
  multiplatform, and documentation. Use when the user is designing or
  reviewing a published API — phrases like "review my public API," "is this
  API idiomatic," "will this break binary compatibility," "API guidelines
  check," "audit my library's surface," or when changing a type that other
  modules or external consumers depend on. This is the library-authoring
  counterpart to the always-on idiom rules: those govern how to write Kotlin,
  this governs how to expose it.
---

# Kotlin API Review

For library / published API surface only. Application and internal code is governed by the always-on idiom rules (`use-data-class`, `nullable-question-mark`, `extension-over-util`, `avoid-boolean-arguments`, `read-only-collections`, `require-check-validation`, `sealed-for-closed-hierarchies`, …), not this skill. The first job is to decide whether the code under review is actually public API.

Process steps in order.

## Step 0 — Confirm The Surface Is Public

- Identify what is genuinely exposed: `public` declarations, `@PublishedApi` (inlined into consumer bytecode — treat as public), multiplatform `expect`/`actual`, anything other modules or external users depend on
- `internal` / `private` declarations are out of scope — report and skip them
- If nothing here is published API, stop and say so: the idiom rules already cover internal code

## Step 1 — Backward Compatibility (the highest-stakes checks)

First, know which kind of compatibility a finding breaks — name it in the report:

- **Binary** — already-compiled client code keeps linking against the new version (no `NoSuchMethodError`). Hardest to preserve, most important for a published library
- **Source** — client code recompiles unchanged against the new version. Desirable but an aspiration, not a promise
- **Behavioral** — same features, same semantics; new version only fixes bugs
- Binary and source compat are independent — one can break while the other holds

Flag, per declaration:

- **Implicit return types** on public functions/properties — refactoring can silently change them, breaking binary compat. Require explicit return types. Recommend turning on **Explicit API mode** (`explicitApi()` in the Kotlin compiler options), which makes the compiler enforce explicit return types and visibility modifiers across the public surface
- **Default arguments** on public functions — they change the JVM bytecode signature; adding one later breaks binary compat (old compiled callers hit `NoSuchMethodError`). Prefer manual overloads (`@JvmOverloads` does NOT preserve binary compat)
- **`data class` in public API** — generated constructor / `copy()` / `componentN()` signatures shift when a property is added or reordered. Prefer a regular class for stable surface (this is the public-API exception to the `use-data-class` rule)
- **Return-type widening/narrowing** — widening (`List` → `Collection`) breaks callers that index; narrowing (`Collection` → `List`, `Number` → `Int`, `Optional<T>` → `T?`) keeps source compat but breaks binary compat. Route through a deprecation cycle, not an in-place swap (the public-API exception to `nullable-question-mark`)
- **New required (non-default) parameters** on existing functions — breaks both binary and source compat
- **`@PublishedApi` declarations** — `internal` members marked `@PublishedApi` get inlined into client bytecode (the mechanism that lets a public `inline` function call them, since inline public functions can't reference plain non-public declarations). They are effectively public — apply every rule above to them
- **Missing deprecation cycle** — breaking changes should go Warning → Error → Hidden → Removal (removal only in a major release), with `@Deprecated(message, replaceWith, level)`; communicate the versioning/deprecation policy to users
- Recommend the [Binary Compatibility Validator](https://github.com/Kotlin/binary-compatibility-validator) (`apiDump` / `apiCheck`, commit the `.api` file; built into the Kotlin Gradle plugin 2.2.0+) if not already wired in
- Recommend `@RequiresOptIn` markers (Preview/Experimental/Delicate) for unstable surface, each category documented in KDoc; **propagate** the marker when the library itself consumes an experimental API from a dependency. Do NOT use opt-in to deprecate — that's `@Deprecated`'s job

## Step 2 — Simplicity

- **Minimize the number of components** — fewer types, functions, and parameters mean less to learn; don't expose a type or option that earns its keep only in a rare case
- **Reuse existing Kotlin types** — accept and return stdlib types (`List`, `Map`, `Duration`, `Result`, `Sequence`) instead of inventing wrappers that consumers must learn and convert to
- **Build on core abstractions** — define a small set of core concepts and layer additional functionality on top (extensions, composition) rather than a wide flat surface of unrelated entry points

## Step 3 — Predictability

- **Do the right thing by default** — the happy path should work with minimal config; supply sensible defaults
- **Allow extension** where the right choice can't be predetermined (extension functions/properties, pluggable strategies)
- **Prevent invalid extension** — `sealed` types over `open` when only specific implementations are valid (the `sealed-for-closed-hierarchies` rule covers this idiom)
- **No exposed mutable state** — return read-only collections, never the live mutable internal (the `read-only-collections` rule covers this idiom)
- **Validate inputs/state** — `require()` for arguments, `check()` for instance state (the `require-check-validation` rule covers this idiom)

## Step 4 — Readability

- **Compose over parameters** — `flow.filter().map().buffer()` beats one function with `filter`/`map`/`buffer` flags
- **DSLs for configuration** — trailing lambda-with-receiver for builder-style config
- **Extensions for layered behaviour** — only core behaviour, operators, and overrides as members; everything else as extensions (mirrors the `extension-over-util` rule)
- **No boolean arguments** — `doWork(true)` is unreadable; split into named functions (`map` / `mapNotNull`) or an `enum` (the `avoid-boolean-arguments` rule covers this idiom)
- **Right numeric type** — `Int`/`Long`/`Double` for arithmetic, `Byte`/`Short`/`Float` for storage constraints, unsigned types for full-positive/interop, inline value classes for IDs and other non-arithmetic entities

## Step 5 — Consistency

- Stable parameter order and naming across similar functions; general-to-specific (essential first, optional last)
- One term per concept throughout (`element` vs `item` vs `entry` — pick one)
- Overloads with different parameter types must be semantically identical (`BigDecimal(200)` == `BigDecimal("200")`)
- Predictable name patterns: `OrNull` suffix → nullable return; `Catching` suffix → exception-wrapping
- One error-handling mechanism across the API (exceptions vs nullable vs `Result<T>`); don't use exceptions for normal control flow

## Step 6 — Debuggability

- Meaningful `toString()` on every stateful type (including internal ones) — no sensitive data, consistent format; document the format only if it's part of the contract. (Note: don't reach for `data class` just to get `toString()` on public API — see Step 1)
- Documented, consistent exception policy: rethrow unchanged when a dependency is intentionally exposed; wrap in a library-specific exception (original via `cause`) when the dependency is an implementation detail

## Step 7 — Testability

- The library AND the code that consumes it must be easy to test
- Provide test doubles (fakes/in-memory implementations) for any type a consumer can't construct or stub themselves — backed by a `sealed`/`interface` seam, not a `final` concrete class
- Don't force a real I/O dependency (network, clock, filesystem) into the only public entry point; allow injection so consumers can test without it
- If you can't document how to test against the API without a live data source, that's a design smell — fix the surface, not the docs

## Step 8 — Multiplatform (only if this is a Kotlin Multiplatform library)

Skip entirely for JVM-only libraries. When the library targets KMP:

- **Place each API in the broadest source set it can live in** — `commonMain` first, then an intermediate set (`concurrent`, `nonJvm`), then a platform set (`androidMain`) only for genuinely platform-exclusive APIs
- **Design for common code** — usable from `commonMain` without platform-specific glue; sensible defaults, platform options only where needed
- **Consistent behaviour across platforms** — same valid inputs, same actions, same results everywhere; isolate divergence behind `expect`/`actual` and document any unavoidable platform difference
- **Maximize target support** — cross-compile (e.g. `.klib` for Apple targets without an Apple machine), tier Kotlin/Native targets
- **Test on every supported platform** — `kotlin-test` for common tests plus the platform runners
- **Non-Kotlin interop** — design types for clean Swift/native access where that's a goal; discoverable via klibs.io

## Step 9 — Documentation

- KDoc on every public entry point — describe behaviour, valid input ranges, behaviour on invalid input, and every exception thrown; restating the signature is not documentation
- Document lambda parameters' exception and concurrency semantics (which thread? parallel? rethrow or wrap?)
- Document by example (inline KDoc snippets), link related APIs (`@see`, mirrored `format`/`parse`)
- Self-contained, simple English; suppress unwanted public APIs from docs (`suppress` directive / minimize surface)

## Step 10 — Report

- One finding per line: `file:line — <category>: <problem>. <fix>.`
- Lead with Step 1 (backward-compat) findings — those are the ones that bite consumers hardest
- Do not auto-apply breaking changes. Propose the deprecation-cycle path and let the operator decide. Non-breaking fixes (add explicit return type, add KDoc, `require()` a precondition) can be applied directly
