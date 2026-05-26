---
alwaysApply: true
---

# Sealed Types For Closed Hierarchies

## When The Set Of Subtypes Is Known, Make It `sealed`

- A `sealed class` or `sealed interface` tells the compiler the complete set of direct subtypes is known at compile time and lives in the same module
- This is the idiomatic Kotlin model for "one of a fixed set of shapes": a `Result` that is `Success` or `Failure`, a `JsonElement` that is `JsonObject` / `JsonArray` / `JsonPrimitive` / `JsonNull`, a UI `State` that is `Loading` / `Loaded` / `Error`
- Reach for `enum` when the cases carry no per-case data; reach for `sealed` when each case needs its own properties

## The Payoff Is Exhaustive `when`

- A `when` over a sealed type needs no `else` branch — the compiler knows every case
- Add a new subtype and every `when` that didn't handle it becomes a compile error, pointing you at exactly the code to update. An `else` branch (or an `open` hierarchy) silently swallows that signal
- Don't add an `else` to a sealed `when` just to satisfy a linter — it throws away the exhaustiveness guarantee that made `sealed` worth using

## `sealed` Over `open` Unless Extension Is Genuinely Open-Ended

- `open` says "anyone, anywhere, may add a subtype" — correct for a plugin SPI, wrong for a domain model with a fixed shape
- An `open` hierarchy that's meant to be closed lets callers construct invalid variants and defeats exhaustiveness. Default to `sealed`; widen to `open` only when third-party extension is an actual goal

## Anti-Patterns

- ❌ `open class PaymentResult` with a `when(result) { … else -> error("?") }` — the `else` hides missing cases
- ✅ `sealed interface PaymentResult { data class Approved(...) ; data class Declined(...) }` with an exhaustive `when`
