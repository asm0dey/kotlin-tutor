---
alwaysApply: true
---

# Avoid Boolean Arguments

## A `Boolean` Parameter Hides Its Meaning At The Call Site

- `file.copy(true)` tells the reader nothing — `true` what? Overwrite? Recursive? Follow symlinks?
- The flag is clear in the declaration and invisible where it's actually used. Readers have to jump to the signature to decode every call
- A second `Boolean` makes it worse: `render(true, false)` is a puzzle, and the two are trivial to swap

## Split Into Named Functions

- Two behaviours behind a flag are usually two functions: `Iterable.map { }` and `Iterable.mapNotNull { }`, not `map(dropNulls = true)`
- The name carries the meaning; there's no parameter to misread
- This composes with extension functions (`extension-over-util`) — each variant reads as its own operation on the receiver

## When You Can't Split, Use An `enum`

- Three or more modes, or a flag that genuinely belongs in the parameter list, becomes an `enum class` — `CaseSensitivity.IGNORE_CASE` reads at the call site; `true` does not
- An `enum` also leaves room to add a third mode later without churning every caller
- If a `Boolean` parameter is unavoidable (interop, a single obvious toggle), require it be passed as a named argument (`overwrite = true`) so the call site stays legible

## Anti-Patterns

- ❌ `connect(host, true)` — what is `true`?
- ❌ `parse(input, true, false)` — two positional booleans, swappable and opaque
- ✅ `connect(host); connectSecure(host)` — or `connect(host, mode = ConnectionMode.TLS)`
