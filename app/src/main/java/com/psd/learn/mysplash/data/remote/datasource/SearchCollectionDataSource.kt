package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchCollectionDataSource (
    private val unSplashApiService: UnSplashApiService,
    queryText: String?,
    override val totalResult: (Int) -> Unit
) : AbsPagingDataSource<CollectionItem>(queryText) {

    override val TAG: String
        get() = SearchCollectionDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        queryText: String?,
        page: Int,
        perPage: Int
    ): List<CollectionItem> {

        if (queryText == null) return emptyList()

        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getSearchCollectionResult(
                query = queryText,
                page = page,
                perPage = perPage
            )
        }
        totalResult(response.total)
        return response.results.map { it.toCollectionItem() }
    }
}