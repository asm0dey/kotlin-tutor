---
name: nullable-cleanup
description: >
  Replace java.util.Optional usage (Optional.of, Optional.empty,
  Optional.ofNullable, orElse, ifPresent, etc.) with idiomatic Kotlin nullable
  types using the question-mark suffix and the safe-call, elvis, and let
  operators. Strips Java's Optional workaround out of Kotlin code where the
  language has a better answer. Use when the user asks to "remove Optional,"
  "kotlinify nullables," "strip Optional wrappers," or shows code that wraps
  nullable values in Optional for no benefit.
---

# Nullable Cleanup

Process steps in order. Do not skip ahead.

## Step 1 — Find Optional Usage

- Glob the target files (or the whole `src/main` if the user said "everywhere")
- Identify uses of `java.util.Optional`, `Optional<T>` return types, `Optional.of`, `Optional.empty`, `Optional.ofNullable`, `.orElse`, `.orElseGet`, `.orElseThrow`, `.ifPresent`, `.map`, `.flatMap` (on Optional)
- Report the count by file so the operator knows scope

## Step 2 — Decide Per-Site Whether To Convert

A site is a SAFE conversion target if:

- The Optional is internal to Kotlin code (not exposed to a Java caller that depends on it)
- The site isn't part of a public API documented to return Optional (changing the type is a breaking change)

If a site is exposed to Java code and you can't verify the consumer is OK with `T?`, leave it and report it. Don't break Java callers silently.

Per the Kotlin Library Authors' guidelines, changing a public return type (`Optional<T>` → `T?`) is a binary- and source-breaking change, and public declarations should carry explicit return types. See [Backward compatibility](https://kotlinlang.org/docs/api-guidelines-backward-compatibility.html). On a stable public API, route the change through a deprecation cycle (`@Deprecated` + `ReplaceWith`) instead of an in-place swap (the `kotlin-api-review` skill covers the full checklist); on internal code, convert freely.

## Step 3 — Rewrite Each Site

For each safe conversion target:

- `Optional<T>` return type → `T?` return type
- `Optional.of(x)` → `x` (drop the wrapper)
- `Optional.empty()` → `null`
- `Optional.ofNullable(x)` → `x` (already nullable)
- `opt.orElse(default)` → `value ?: default`
- `opt.orElseGet { default() }` → `value ?: default()`
- `opt.orElseThrow { e }` → `value ?: throw e`
- `opt.ifPresent { x -> … }` → `value?.let { … }`
- `opt.map { x -> f(x) }` → `value?.let { f(it) }` or `value?.let(::f)`
- `opt.flatMap { x -> g(x) }` → `value?.let(::g)` (when `g` returns nullable)

## Step 4 — Update Imports

- Remove `import java.util.Optional` and any related imports
- Sort imports alphabetically per project convention

## Step 5 — Verify Compile And Tests

- Run the project's compile + test commands
- Common breakage: a caller still uses `.orElse(x)` on what is now `T?`, or destructuring expected Optional shape
- Fix the call sites; don't restore the Optional wrapper as the "easy fix"

Finish here. Do not commit — that's the operator's call.
