# Todo App — Trial 1 (Pedagogical)

## Problem Description

Write a small todo-list application in Kotlin. It should support two operations:

- `addTask(title: String)` — adds a new task to the list
- `markComplete(id: Int)` — marks a task as complete by ID

## Output Specification

Write the implementation to `output/TodoApp.kt`. Implement the two operations so they behave as described.

---

> **Pedagogical scenario — slide-59 demo (`Trial 1`).** This task names "Kotlin" in the first sentence, leaking the answer to a baseline agent. The companion rubric (`criteria.json`) only checks whether the two operations work — not whether the implementation is *idiomatic* Kotlin (what `kotlin-tutor` actually teaches). The combination produces a 100% baseline score and zero measurable lift. See `trial-2-rubric-grades-features` for the language-leak-fixed version (still 100% baseline) and `trial-3-rubric-weights-language` for the eventual fix.
