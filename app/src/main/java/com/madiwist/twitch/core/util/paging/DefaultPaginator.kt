package com.madiwist.twitch.core.util.paging

import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText


class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Resource<List<Item>>,
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (UiText?) -> Unit,
    private val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) : Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        val items = result.data ?: emptyList()
        onLoadUpdated(false)

        if (result is Resource.Error) {
            onError(result.uiText)
            return
        }

        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
    }

    override fun reset() {
        currentKey = initialKey
    }
}
