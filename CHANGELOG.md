# Changelog

All notable changes to `jbaruch/kotlin-tutor` are documented here. The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project adheres to [semantic versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] — 2026-05-13

### Added

- Initial plugin scaffold (`tile.json`, `README.md`, `.tileignore`, `.gitignore`, `.gitattributes`)
- Six steering rules: `prefer-val` (K-1), `nullable-question-mark` (K-2), `use-data-class` (K-3), `kotest-over-junit` (K-4), `prefer-stdlib-scope` (K-5), `extension-over-util` (K-6)
- Three skills: `kotlinify-tests` (JUnit → Kotest conversion), `pojoify-to-dataclass` (regular class → `data class` refactor), `nullable-cleanup` (`Optional<T>` → `T?`)
- One script: `scripts/verify-no-junit-assertions.sh` — CI gate delegated from the `kotlinify-tests` skill; enforces rule K-4 deterministically

### Notes

- This plugin debuts at Geecon 2026 in the talk *"You're Absolutely Right (and Other Lies My AI Told Me)"* as the running engineering example through chapter 3 (slides 22-24, A/B demo at 26-27) and chapter 5 (eval examples)
