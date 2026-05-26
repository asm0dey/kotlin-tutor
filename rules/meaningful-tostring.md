---
alwaysApply: true
---

# Meaningful `toString` On Stateful Types

## A Default `toString` Is A Debugging Dead End

- The inherited `Any.toString()` prints `com.example.Connection@5fd0d5ae` — a hash, not state. In a log line or a debugger watch, that tells you nothing
- Any class that holds state worth inspecting should override `toString()` to render that state. This is the cheapest debuggability win there is
- This is the non-`data class` case. A `data class` already generates a `toString()` that lists its properties (`use-data-class`) — don't hand-roll one there. This rule is for the regular classes that hold state: connections, sessions, builders, stateful services

## Render The State, Skip The Secrets

- Include the fields that identify the instance and explain its current condition: `Subscription(plan=PRO, status=ACTIVE, renews=2026-06-01)`
- Never put passwords, tokens, keys, or other sensitive data in `toString()` — it leaks into logs and stack traces. Mask them: `token=***`
- This is the one legitimate reason to hand-write `toString()` on a `data class` too — override only `toString()` to mask a sensitive field, and leave `equals` / `hashCode` generated (`use-data-class`)

## Keep It Consistent And Cheap

- Use a consistent shape across your types — `TypeName(field=value, …)` mirrors the generated `data class` format and reads predictably
- `toString()` may be called from a logger on a hot path; keep it allocation-light and never let it throw
- Only treat the format as a stable contract if you document it as one — otherwise you're free to change it

## Anti-Patterns

- ❌ a stateful `class Session(...)` with no `toString()` — logs print `Session@1b6d3586`
- ❌ `toString()` that interpolates `password` or an auth `token`
- ✅ `override fun toString() = "Session(user=$userId, state=$state)"`
