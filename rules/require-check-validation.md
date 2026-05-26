---
alwaysApply: true
---

# Validate With `require` And `check`

## Fail Fast At The Boundary

- A function that receives bad input should say so immediately, with a clear message — not limp along and throw a confusing `NullPointerException` three frames deep
- Kotlin's stdlib gives you two precondition functions that read as intent and throw the right exception type. Use them instead of hand-rolled `if (…) throw IllegalArgumentException(…)`

## `require` For Arguments, `check` For State

- `require(condition) { "message" }` validates **inputs** — throws `IllegalArgumentException`. Use it for "the caller gave me something invalid"
- `check(condition) { "message" }` validates **instance state** — throws `IllegalStateException`. Use it for "this object isn't in a state where this call makes sense"
- `requireNotNull(x) { … }` and `checkNotNull(x) { … }` both validate and smart-cast to the non-null type in one step
- The message lambda is only evaluated on failure, so building a detailed message is free on the happy path

## Put The Offending Value In The Message — Except Secrets

- `require(count > 0) { "count must be positive, was $count" }` tells whoever reads the log exactly what went wrong
- Never interpolate passwords, tokens, or other sensitive data into the message — it lands in logs and stack traces

## Anti-Patterns

- ❌ `if (name.isBlank()) throw IllegalArgumentException("bad name")` — reinventing `require`
- ❌ `require(user.token == expected)` — leaks the token into the exception message on failure
- ✅ `require(name.isNotBlank()) { "name must not be blank" }`
- ✅ `check(isInitialized) { "call start() before send()" }`
