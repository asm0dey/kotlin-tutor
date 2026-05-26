package com.platform.cache

class CacheService(private val redisUrl: String, private val defaultTtlSeconds: Int) {

    fun get(key: String, forceRefresh: Boolean): String? {
        TODO("fetch from Redis, bypass cache layer when forceRefresh is true")
    }

    fun getAll(prefix: String): MutableList<String> {
        TODO("return all values whose keys start with prefix")
    }

    fun put(key: String, value: String) {
        TODO("write to cache with defaultTtlSeconds TTL")
    }

    fun put(key: String, value: String, ttlSeconds: Int) {
        TODO("write to cache with explicit TTL")
    }

    fun fetchItem(key: String): String {
        TODO("throws NoSuchElementException if key not found")
    }

    fun getItem(key: String): String? {
        TODO("returns null if key not found")
    }

    fun invalidate(key: String, cascade: Boolean) {
        TODO("remove key; when cascade is true, also remove all keys sharing the same prefix segment")
    }

    fun stats(): Map<String, Long> {
        TODO("return hit/miss/eviction counters")
    }
}
