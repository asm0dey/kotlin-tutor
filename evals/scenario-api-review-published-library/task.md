# Notification Library API Review

## Background

Your team maintains `acme-notify`, a Kotlin library published to Maven Central and used by several downstream services and third-party integrators. Version 1.5 is scheduled for release next month and will add new fields to the notification model, expand batch capabilities, and expose delivery status reporting. Before cutting the release branch, the team lead has asked for a thorough review of the public API to catch any issues that could break existing consumers upgrading from 1.4.

The library source you need to review is in `inputs/NotificationClient.kt`. External consumers compile against the published JAR, so binary compatibility matters — a `NoSuchMethodError` at runtime is far worse than a compiler error.

## What to Produce

Perform a full API review of the file and write your findings to `api-review.md`. Your review should:

- Identify which concerns apply given the library's exposure level
- Cover all relevant review dimensions for this surface
- Include any tooling or process recommendations that would reduce the risk of accidental breaking changes in future releases
