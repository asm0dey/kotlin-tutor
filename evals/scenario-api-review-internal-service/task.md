# Caching Service API Review

## Background

The platform team has built a caching service that three product teams are about to integrate into their own modules. Before those teams write code against it, the tech lead has asked for a design review of the API. The service is internal — it is not published as a standalone library and does not cross process boundaries — but because multiple teams will depend on it, design problems now mean migration pain later.

The source file is in `inputs/CacheService.kt`. The implementation bodies are placeholders; your review should focus on the API surface, not the implementation.

## What to Produce

Write your findings to `api-review.md`. Your review should cover the full range of API design concerns appropriate for this surface — think about how callers will use each entry point, how they will test their own code in the presence of this dependency, and whether the surface is internally consistent. Propose concrete alternatives where you see problems. Non-breaking improvements may be applied directly as edits to `inputs/CacheService.kt`; structural design proposals that would require callers to change should be described in `api-review.md` rather than applied.
