# Test Suite Modernisation and Verification

## Problem Description

The payments module has been modernising its test files to match current project conventions. The test source tree under `src/test/kotlin/` still contains a mix of older code and newer code, and the team wants the remaining older files brought in line with the conventions.

Your job is to find the test files that don't match current conventions, convert them, and verify the result is clean by running whatever check tooling the project provides. If a check reports failures, fix the remaining issues and re-run until it exits cleanly.

## Output Specification

- Convert the test files in place (modify them within `src/test/kotlin/`)
- Write a `conversion-report.md` that includes:
  - The list of files you found and converted
  - The output and exit code of any check tool you ran as the completion gate
