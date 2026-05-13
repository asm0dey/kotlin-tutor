# Calculator Test Migration

## Problem Description

The core platform team maintains a Calculator utility class that has been covered by JUnit 5 tests since the early days of the project. The team has decided to modernise the test suite to match current project conventions, and the Calculator tests are next in the queue.

The test file is at `src/test/kotlin/com/example/CalculatorTest.kt`. It covers the usual arithmetic operations and includes a few edge cases — division by zero, optional square roots, and sign checks. The Calculator implementation class does not need to change; only the test file should be updated.

## Output Specification

Write the converted test file to `output/CalculatorTest.kt`. Every existing test case must be preserved with its original intent intact. Only the test infrastructure and assertion style should change.
