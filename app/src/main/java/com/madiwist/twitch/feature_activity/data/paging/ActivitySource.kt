package com.madiwist.twitch.feature_activity.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.madiwist.twitch.core.domain.models.Activity
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_activity.data.remote.ActivityApi
import retrofit2.HttpException
import java.io.IOException

class ActivitySource (
    private val api: ActivityApi,
) : PagingSource<Int, Activity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Activity> {
        return try {
            val nextPage = params.key ?: 0
            val activities = api.getActivities(
                page = nextPage,
                pageSize = Constants.DEFAULT_PAGE_SIZE
            )
            LoadResult.Page(
                data = activities.map { it.toActivity() },
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (activities.isEmpty()) null else nextPage + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Activity>): Int? {
        return state.anchorPosition
    }
}