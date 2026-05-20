---
alwaysApply: true
---

# Koog for AI Agents on the JVM

## Koog Is the Kotlin-Native Agent Framework

- For building AI agents, agent-driven pipelines, sub-agent decomposition, and multi-agent orchestration on JVM, use **[Koog](https://github.com/JetBrains/koog)** (`ai.koog:koog-agents`, current **0.7.x**)
- Koog is JetBrains' Kotlin-native AI agent framework — type-safe DSL, MCP integration, multiplatform targets, multi-LLM providers (Anthropic, OpenAI, Google, Ollama)
- Don't reach for Python-only frameworks (Claude Agent SDK, LangChain, AutoGen, CrewAI) if your application is Kotlin/JVM. The Python orchestrator + Kotlin sub-agents split is a smell.

## Versions

- **Koog 0.7.3+** requires **Kotlin 2.3.x** (older Kotlin produces metadata-incompatible classes)
- Pin via: `implementation("ai.koog:koog-agents:0.7.3")`

## Idiomatic Use

- An `AIAgent` is a value with a `promptExecutor`, `llmModel`, optional `systemPrompt`, and optional `toolRegistry`
- `agent.run(input)` is `suspend` — call from `runBlocking { }` at the top level
- Sub-agent pattern: wrap an agent as a tool via `AIAgentService.fromAgent(...).createAgentTool(...)`, then register that tool in the parent's `ToolRegistry`
- Each sub-agent invocation gets a **fresh context** — pass anything the sub-agent needs explicitly via its system prompt (this is the "context engineering for the orchestrator" pattern; see `jbaruch/sub-agent-delegation`)

## LLM Provider

- For Anthropic: `simpleAnthropicExecutor(System.getenv("ANTHROPIC_API_KEY"))` + `AnthropicModels.Sonnet_4` (or `Opus_4_*`)
- For OpenAI: `simpleOpenAIExecutor(...)` + `OpenAIModels.Chat.GPT4o`
- For Google: `simpleGoogleExecutor(...)` + the relevant Gemini model
- For local: configure via Ollama integration

## Structured Output

- When sub-agents need to return decisions for downstream code, prompt them to emit JSON with a schema, then parse with `kotlinx-serialization-json`. This is more robust than free-form natural language responses.

## Anti-patterns

- ❌ Spawning a Python subprocess (`anthropic-sdk-python`, `claude-agent-sdk`) from a Kotlin app to orchestrate sub-agents
- ❌ Hand-rolling agent loops on top of raw `HttpClient` calls to Anthropic — you'll reinvent retries, conversation history, tool calling, and break before you ship
- ❌ Using `GlobalScope.launch { agent.run(...) }` — unowned scope, leaks; use structured concurrency
- ❌ Sharing a `Predictor` or LLM client across non-coroutine threads
