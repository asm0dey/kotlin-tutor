#!/usr/bin/env bash
#
# verify-no-junit-assertions.sh
#
# Fails CI if any Kotlin test file under src/test/ still uses JUnit-style
# assertions (assertEquals, assertTrue, assertFalse, assertNull, assertNotNull,
# assertThrows). Delegated from the `kotlinify-tests` skill; also safe to run
# standalone as a CI gate on every commit.
#
# Rule enforced: K-4 kotest-over-junit (see rules/kotest-over-junit.md)
#
# Exit codes:
#   0 — no JUnit assertions found, conversion is clean
#   1 — at least one JUnit assertion present, diagnostic on stderr
#   2 — invocation error (target directory missing, etc.)

set -euo pipefail

TEST_DIR="${1:-src/test}"

if [ ! -d "$TEST_DIR" ]; then
  echo "verify-no-junit-assertions: $TEST_DIR is not a directory" >&2
  echo "usage: $0 [test-dir]  (default: src/test)" >&2
  exit 2
fi

# Match JUnit-style assertion calls. Word boundary on both sides to avoid
# false positives like `customAssertEquals` in user code.
PATTERN='\bassert(Equals|NotEquals|True|False|Null|NotNull|Same|NotSame|Throws|DoesNotThrow|Timeout|All)\b'

# Collect violations into a temp file; show them all rather than fail on first.
hits=$(grep -rEn "$PATTERN" "$TEST_DIR" --include='*.kt' || true)

if [ -n "$hits" ]; then
  echo "verify-no-junit-assertions: K-4 violation — JUnit-style assertions found:" >&2
  echo "" >&2
  echo "$hits" >&2
  echo "" >&2
  echo "Convert these to Kotest matchers (shouldBe / shouldNotBe / shouldThrow / etc.)" >&2
  echo "See rules/kotest-over-junit.md or run the kotlinify-tests skill." >&2
  exit 1
fi

echo "verify-no-junit-assertions: OK ($TEST_DIR clean of JUnit assertions)"
exit 0
