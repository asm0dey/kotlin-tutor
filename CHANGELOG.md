# Changelog

All notable changes to `jbaruch/kotlin-tutor` are documented here. The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project adheres to [semantic versioning](https://semver.org/spec/v2.0.0.html).

## [0.7.0] — 2026-05-21

### Added

- **New idiom rule K-7: `stateflow-over-atomic-polling`** — prefer `MutableStateFlow<T?>` (or `MutableStateFlow<T>` with a sensible default when one exists) over `AtomicReference<T?>` + a `delay`-based poll loop for single-writer / many-reader reactive state. Reported in [#5](https://github.com/jbaruch/kotlin-tutor/issues/5) from a Ktor MJPEG-streaming review where the agent had produced the textbook anti-pattern (`while (true) { val x = ref.get(); if (x == null) delay(20); continue }` with `latest.set(...)` in the writer); the rule is the highest-frequency miss when an agent trained on pre-coroutines JVM material ports reactive state into a coroutine codebase. Rule covers the trigger smell (the busy-poll loop literal), the StateFlow replacement (nullable variant for the `AtomicReference<T?>` 1:1 swap, non-null variant when a default exists), when `AtomicReference` remains correct (lock-free CAS coordination across non-coroutine threads), and the `@Volatile var` + polling variant of the same anti-pattern
- Idiom-rule count grows from six to seven; total rule count grows from twelve to thirteen. README rules table, intro paragraph, and Conventions section all updated to reflect K-7 + the new rule total

### Notes

- Issue #5 also flagged two micro-idioms ("hoist `.toByteArray(charset)` to `private val`", "`lateinit var` over `var ... = null` when a `start()` gate exists"). Deferred — too small to anchor their own `alwaysApply` rules at the load this plugin already carries (every front-loaded rule costs context budget on every prompt; 1.2k tokens for `koog-for-agents` is already the largest single line item per `tessl tile lint`). Will revisit if they recur in field reports

## [0.6.1] — 2026-05-21

### Fixed

- `rules/koog-for-agents.md` "Idiomatic Use" section was still describing the 0.7.x API after the 0.6.0 version pin bump. Reported in [#3](https://github.com/jbaruch/kotlin-tutor/issues/3) from a real migration of `jbaruch/robocoders-kagematch` to `1.0.0-preview3` (commit [`256b7c1`](https://github.com/jbaruch/robocoders-kagematch/commit/256b7c1)) where the rule's wording cost ~20 minutes of jar-disassembly before the correct surface was found. Rewrote "Idiomatic Use" as two version-explicit sections covering the gaps the issue called out:
  - **`simpleAnthropicExecutor(...)` is gone in 1.0** — replacement is `PromptExecutor.builder().anthropic(apiKey).build()` (same shape for `.openAI(...)`, `.google(...)`, `.ollama(...)`)
  - **`AIAgent(...)` now requires `strategy`** — the 0.7.x signature without `strategy` does not compile against 1.0; `AIAgent` is an abstract class and `AIAgent(...)` is a top-level factory function
  - **`functionalStrategy<I, O> { input -> ... }`** is the simplest strategy primitive, with three pitfalls now documented: single-arg lambda (`this` is the context, not a second param), `Message.Assistant.parts: List<MessagePart>` extraction (not `.content` / `.text`), and avoid the Java `AIAgent.builder().functionalStrategy(BiFunction<...>)` overload from Kotlin (it's blocking)
  - **Don't import `anthropicClient(...)` directly** — the `AnthropicClientFactory` bytecode advertises it as a top-level facade but it isn't resolvable from Kotlin source against Kotlin 2.3.0; use the executor builder instead
  - **0.8.0 stable fallback** now has its own subsection making the "if you pin 0.8.0, here's the code that still works" path explicit and contrasting it against the 1.0 sample

## [0.6.0] — 2026-05-21

### Changed

- **Koog version pin bumped from `0.7.3` to `1.0.0-preview3`** in `rules/koog-for-agents.md`. JetBrains had positioned both Kotlin 2.4 GA and Koog 1.0 GA for KotlinConf 2026-05-21 but neither landed in time; `1.0.0-preview3` (2026-05-20) is the latest release candidate ahead of the upcoming 1.0 GA. The 1.0 preview line splits modules into stable (`1.0.0-preview*`) and beta (`1.0.0-beta-preview*`) streams so production code can pin to APIs that won't break without a deprecation cycle. Kotlin guidance stays at 2.3.x — the latest stable Kotlin remains `2.3.21`; Kotlin `2.4.0-RC` exists but no 2.4 GA yet
- Migration callouts added to `rules/koog-for-agents.md` for consumers coming from 0.7.x / 0.8.0: HTTP transport decoupled from Ktor (`KoogHttpClient.Factory`), `AgentMemory` feature removed in favor of `LongTermMemory`, Java blocking methods renamed to `*Blocking`, planners moved to a separate `agents:agents-planners` module
- `rules/koog-for-agents.md` now states the JDK floor explicitly — Koog 1.0 sets the minimum at **JDK 17** (JDK 21 still recommended for parity with JavaCV 1.5.13 native libs); the previous rule text implied JDK 21 was required, which was always stricter than Koog actually demanded
- `rules/kotlin-stack-defaults.md` "avoid Kotlin 2.1.x" guidance updated to cover the Koog 1.0 preview line as well as 0.7

## [0.5.0] — 2026-05-21

### Added

- **Six stack-default rules** covering canonical JVM choices alongside the existing Kotlin-idiom rules: `kotlin-stack-defaults` (S-1), `coroutines-for-concurrency` (S-2), `ktor-for-http` (S-3), `djl-for-jvm-ml` (S-4), `javacv-for-vision` (S-5), `koog-for-agents` (S-6). The plugin now teaches both "write idiomatic Kotlin" AND "pick the right Kotlin library on JVM"
- Twelve `alwaysApply` rules total (six idiom + six stack); README rules table split into two sub-sections to reflect the two concerns

## [0.4.0] — 2026-05-13

### Fixed

Cleaned several leaks in the `trial-*` scenarios that were silently coupling baseline to with-plugin scores by tipping the agent off about the expected output:

- Task bodies declared operations using Kotlin type-annotation syntax (`addTask(title: String)`, `markComplete(id: Int)`); rewritten as prose so the task no longer hints at a language
- Output specifications said `output/TodoApp.kt`; changed to `output/TodoApp.<ext>` so the file extension follows the agent's language choice rather than dictating it
- Each `task.md` ended with a long trailing blockquote describing the scenario's design rationale, which the agent reads as part of its task context; removed
- `scenario.json` descriptions previously described the rubric mechanics, which can surface in agent context depending on how the eval host packages the scenario; trimmed to a neutral one-line summary
- Several `criteria.json` checklist descriptions referenced internal rule identifiers; replaced with descriptions of the criterion's content alone

`trial-1` retains the explicit language-name leak in its task; that is the intended distinguishing feature versus `trial-2`. The trial-1 → trial-2 diff is now solely the one phrase that should differ.

## [0.3.0] — 2026-05-13

### Added

- **Two real-eval scenarios** closing the per-skill coverage gap flagged by `jbaruch/coding-policy`'s `plugin-evals` rule:
  - `evals/scenario-pojoify-subscription/` — exercises `pojoify-to-dataclass` (Java-style `Subscription` POJO with manual equals/hashCode/toString + bean accessors → idiomatic data class). Criteria check K-1 + K-3 specifics and field-order preservation
  - `evals/scenario-nullable-user-service/` — exercises `nullable-cleanup` (`UserRepository` returning `Optional<User>` with `.orElse` / `.ifPresent` / `.map` / `.orElseThrow` + `UserService` caller that must update alongside). Criteria check K-2 mappings and the no-`!!` invariant
- All three skills now have at least one dedicated scenario (previously only `kotlinify-tests` was covered)

### Fixed

- `nullable-cleanup` skill description was failing deterministic skill-review validation (`description must not contain XML tags`) because `Optional<T>` was parsing as an XML tag. Rephrased to use `java.util.Optional` and prose-named operators (`question-mark suffix`, `safe-call`, `elvis`, `let`). Skill review score: 0% (FAILED) → 100%
- `scenario-1/task.md` bled the spec-style answer into the task — phrases like "each test is standalone" and "structured narrative pattern" directly told the agent which Kotest spec to pick. Task rewritten to require the agent to read the test files themselves to determine structure
- `skills/pojoify-to-dataclass/SKILL.md` review score was 88% (barely passing). Ran `tessl skill review --optimize`, curated the diff per `coding-policy`'s "Disagreeing With the Reviewer" rule (kept the before/after example and prose-tightening; restored the literal "Do not skip ahead" line `skill-authoring` prescribes, the `componentN()` why-this-matters context, "Glob" over "Grep", and the `use-data-class` cross-reference). New score: 100%

### Notes

- The `coding-policy` audit that surfaced these issues lives at `jbaruch/coding-policy@0.3.20`. Recursive demonstration: the talk's META plugin found three real gaps in the talk's running-example plugin, and fixing them is documented in this changelog entry and the git log. The talk's chapter 6 META reveal at slide 70 references this iteration as live evidence the prescription pays rent

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
