package com.madiwist.twitch.core.util.paging

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}