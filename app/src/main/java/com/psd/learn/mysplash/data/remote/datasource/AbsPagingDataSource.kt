package com.psd.learn.mysplash.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.psd.learn.mysplash.NETWORK_PAGE_SIZE
import com.psd.learn.mysplash.UNSPLASH_STARTING_PAGE_INDEX
import com.psd.learn.mysplash.utils.log.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import retrofit2.HttpException
import java.io.IOException

abstract class AbsPagingDataSource<T : Any>(
    private val queryText: String? = null
) : PagingSource<Int, T>() {

    protected open val TAG = AbsPagingDataSource::class.java.simpleName

    abstract suspend fun getListDataPaging(
        queryText: String?,
        page: Int,
        perPage: Int
    ): List<T>

    open val totalResult: (Int) -> Unit = {}

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            val resultPage = state.closestPageToPosition(anchorPos)
            resultPage?.prevKey?.plus(1)
                ?: resultPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val listDataInfo =
                getListDataPaging(queryText = queryText, page = position, perPage = params.loadSize)
            val nextKey =
                if (listDataInfo.isEmpty()) null else (position + (params.loadSize / NETWORK_PAGE_SIZE))

            LoadResult.Page(
                data = listDataInfo,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            Logger.e(TAG, "load() - IOException error: $e")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Logger.e(TAG, "load() - HttpException error: $e")
            LoadResult.Error(e)
        }
    }
}