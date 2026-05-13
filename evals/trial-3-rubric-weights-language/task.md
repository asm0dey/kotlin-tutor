# Todo App — Trial 3 (Pedagogical)

## Problem Description

Write a small todo-list application. It should support two operations:

- `addTask(title: String)` — adds a new task to the list
- `markComplete(id: Int)` — marks a task as complete by ID

## Output Specification

Write the implementation to `output/TodoApp.kt`. Implement the two operations so they behave as described.

---

> **Pedagogical scenario — slide-59 demo (`Trial 3`).** Same language-neutral task as trial-2, but the rubric (`criteria.json`) now weights *idiomatic Kotlin* at 80% and features at 10% each. The plugin's value (knowing the language *and* the idioms — `data class` for the task record, `val` for properties, `String?` for nullable fields, no manual equals/hashCode) is now what the rubric measures. Baseline (no plugin loaded) picks any language plausible from the file extension hint or defaults to something idiomatic to *its* preferences — drops to ~20%. With `kotlin-tutor` loaded — reaches ~95%. **Lift is now measurable.** This is the eval the plugin actually deserves; trials 1 and 2 are warning examples.
