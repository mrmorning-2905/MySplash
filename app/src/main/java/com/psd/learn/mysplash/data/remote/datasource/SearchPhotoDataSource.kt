package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchPhotoDataSource(
    private val unSplashApiService: UnSplashApiService,
    queryText: String?,
    override val totalResult: (Int) -> Unit
) : AbsPagingDataSource<PhotoItem>(queryText) {

    override val TAG: String
        get() = SearchPhotoDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        queryText: String?,
        page: Int,
        perPage: Int
    ): List<PhotoItem> {

        if (queryText == null) return emptyList()

        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getSearchPhotoResult(
                query = queryText,
                page = page,
                perPage = perPage
            )
        }
        totalResult(response.total)
        return response.results.map { it.toPhotoItem() }
    }
}