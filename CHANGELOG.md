# Changelog

All notable changes to `jbaruch/kotlin-tutor` are documented here. The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project adheres to [semantic versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.0] — 2026-05-13

### Added

- Three **pedagogical** eval scenarios under `evals/trial-1-task-leaks-language/`, `evals/trial-2-rubric-grades-features/`, `evals/trial-3-rubric-weights-language/`. These are *deliberately instructive* — trial 1 leaks the language name in the task and grades features only (baseline scores 100%); trial 2 fixes the task but leaves the broken rubric (baseline still 100%); trial 3 weights idiomatic Kotlin at 80% and features at 10% each (baseline drops, lift is measurable). Demonstrates the slide-59 worked example of the talk "You're Absolutely Right (and Other Lies My AI Told Me)" with real, runnable scenarios anyone can inspect in the registry.

### Notes

- The pedagogical scenarios are flagged in their `task.md` and `criteria.json` `context` fields so anyone browsing the registry understands why they look strange — trials 1 and 2 are *intentionally bad eval design*; trial 3 is the fix.
- The three originally-generated scenarios from 0.1.0 (scenario-0, scenario-1, scenario-2) are the *real* evals; the trial-N scenarios are talk material.

## [0.1.0] — 2026-05-13

### Added

- Initial plugin scaffold (`tile.json`, `README.md`, `.tileignore`, `.gitignore`, `.gitattributes`)
- Six steering rules: `prefer-val` (K-1), `nullable-question-mark` (K-2), `use-data-class` (K-3), `kotest-over-junit` (K-4), `prefer-stdlib-scope` (K-5), `extension-over-util` (K-6)
- Three skills: `kotlinify-tests` (JUnit → Kotest conversion), `pojoify-to-dataclass` (regular class → `data class` refactor), `nullable-cleanup` (`Optional<T>` → `T?`)
- One script: `scripts/verify-no-junit-assertions.sh` — CI gate delegated from the `kotlinify-tests` skill; enforces rule K-4 deterministically

### Notes

- This plugin debuts at Geecon 2026 in the talk *"You're Absolutely Right (and Other Lies My AI Told Me)"* as the running engineering example through chapter 3 (slides 22-24, A/B demo at 26-27) and chapter 5 (eval examples)
