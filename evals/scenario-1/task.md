# Test Suite Migration for Core Utilities

## Problem Description

The platform team maintains test suites for two components: a StringFormatter utility and a UserService. Both were written in JUnit 5 some time ago, and the team has been modernising older test files to fit current project conventions.

The files are:

- `inputs/StringFormatterTest.kt` — tests for individual text-transformation methods; each test is standalone and exercises a single formatting operation independently of the others
- `inputs/UserServiceTest.kt` — tests that describe user management workflows; the method names follow a structured narrative pattern

Each file has a different character, and the team expects the migration to honour those structural differences — not just mechanically port annotations.

## Output Specification

Write the converted files to `output/StringFormatterTest.kt` and `output/UserServiceTest.kt`.

Every existing test case must be preserved with its intent intact. The setup logic in `StringFormatterTest` must continue to run before each test.
