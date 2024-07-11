package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchPhotoDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    queryText: String?,
    override val totalResult: (Int) -> Unit
) : AbsPagingDataSource<PhotoItem>(queryText) {

    override val TAG: String
        get() = SearchPhotoDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<PhotoItem> {

        if (query == null) return emptyList()

        val response = withContext(coroutineDispatcher) {
            unSplashApiService.getSearchPhotoResult(
                query = query,
                page = page,
                perPage = perPage
            )
        }
        totalResult(response.total)
        return response.results.map { it.toPhotoItem() }
    }
}