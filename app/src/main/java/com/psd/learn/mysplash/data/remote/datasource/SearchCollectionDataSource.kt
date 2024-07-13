package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchCollectionDataSource (
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    queryText: String?,
    override val totalResult: (Int) -> Unit
) : AbsPagingDataSource<CollectionItem>(queryText) {

    override val TAG: String
        get() = SearchCollectionDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<CollectionItem> {

        if (query == null) return emptyList()

        return withContext(coroutineDispatcher) {
            val response = unSplashApiService.getSearchCollectionResult(
                query = query,
                page = page,
                perPage = perPage
            )
            totalResult(response.total)
            response.results.map { it.toCollectionItem() }
        }
    }
}