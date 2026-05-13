---
alwaysApply: true
---

# Nullable Question Mark

## Use `?`, Not `Optional`

- Kotlin's nullable type — `String?`, `User?`, `List<String>?` — is the idiom for "this value may be absent"
- `Optional<T>` is the Java workaround for a language without nullable types. Kotlin has them. Use them.
- Returning `Optional<String>` from a Kotlin API forces callers to interop with Java's wrapper for no benefit; return `String?` and let `?.` / `?:` do the work

## Reach For the Operators

- `?.` for safe navigation: `user?.profile?.email`
- `?:` for fallback: `name ?: "anonymous"`
- `?.let { … }` to run a block only when the value is present
- `!!` is reserved for cases where null would be a bug and you want a clear NPE — never use it to silence the compiler

## Java Interop Stays Honest

- When consuming a Java method that may return null, type the result as `T?` and let the type system protect you
- Don't paper over Java's lack of null annotations with `!!` or platform types — that's how `NullPointerException`s smuggle into Kotlin code
- Annotate Kotlin APIs called from Java with `@JvmName` and clear nullability so the Java side sees the contract too
