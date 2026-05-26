---
alwaysApply: true
---

# Read-Only Collection Types In Signatures

## Default To `List` / `Set` / `Map`, Not The `Mutable` Variants

- Kotlin separates the read-only interface (`List<T>`) from the mutable one (`MutableList<T>`). A function signature should ask for and hand back the narrowest type that does the job — usually the read-only one
- Returning `List<T>` says "this is yours to read." Returning `MutableList<T>` invites a caller to mutate a collection you may still own, and silently couples them to your internal representation
- Accept `Collection<T>` / `Iterable<T>` for inputs you only iterate — it lets callers pass whatever they have

## Don't Expose Internal Mutable State

- A property backed by a `mutableListOf()` should expose it as `List<T>`, not the mutable reference — otherwise any caller can edit your internal state behind your back
- Either expose a read-only view (`val items: List<T> get() = _items`) or return a copy (`_items.toList()`); pick based on whether callers must see live updates
- Read-only `List<T>` is not the same as immutable — it's a view. If the backing collection mutates, the view reflects it. When callers need a stable snapshot, return `.toList()`

## Avoid Arrays In Public Signatures

- `Array<T>` is always mutable and has reference equality — prefer `List<T>`
- `vararg` is the exception: with the spread operator Kotlin already makes a defensive copy, so `vararg` parameters are safe

## Anti-Patterns

- ❌ `fun getTags(): MutableList<String> = tags` — hands out the live internal list
- ❌ `fun process(items: ArrayList<Item>)` — demands a concrete mutable type the caller may not have
- ✅ `fun getTags(): List<String> = tags.toList()` and `fun process(items: Collection<Item>)`
