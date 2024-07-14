package com.psd.learn.mysplash.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.psd.learn.mysplash.PAGING_SIZE
import com.psd.learn.mysplash.START_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

abstract class AbsPagingDataSource<T : Any>(
    private val query: String? = null,
    open val totalResult: (Int) -> Unit = {}
) : PagingSource<Int, T>() {

    protected open val TAG = AbsPagingDataSource::class.java.simpleName

    abstract suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<T>>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            val resultPage = state.closestPageToPosition(anchorPos)
            resultPage?.prevKey?.plus(1)
                ?: resultPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val position = params.key ?: START_PAGE_INDEX
        return getResultPagingData(query, position, params.loadSize)
            .fold(
                onSuccess = { listDataInfo ->
                    val nextKey =
                        if (listDataInfo.isEmpty()) null else (position + (params.loadSize / PAGING_SIZE))
                    LoadResult.Page(
                        data = listDataInfo,
                        prevKey = if (position == START_PAGE_INDEX) null else position - 1,
                        nextKey = nextKey
                    )
                },
                onFailure = {
                    LoadResult.Error(it)
                }
            )
    }
}