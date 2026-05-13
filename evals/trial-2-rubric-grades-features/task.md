# Todo App — Trial 2 (Pedagogical)

## Problem Description

Write a small todo-list application. It should support two operations:

- `addTask(title: String)` — adds a new task to the list
- `markComplete(id: Int)` — marks a task as complete by ID

## Output Specification

Write the implementation to `output/TodoApp.kt`. Implement the two operations so they behave as described.

---

> **Pedagogical scenario — slide-59 demo (`Trial 2`).** The task no longer names a language — the agent has to choose one. But the rubric (`criteria.json`) still only checks whether the two operations work. Any competent agent picks *some* language and implements the operations correctly. Baseline still scores 100%. The plugin's value (idiomatic Kotlin) is invisible to the rubric. See `trial-3-rubric-weights-language` for the fix that makes lift measurable.
