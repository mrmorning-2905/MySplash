package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollectionDetailsDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    query: String?
) : AbsPagingDataSource<PhotoItem>(query) {

    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<PhotoItem> {
        if (query == null) return emptyList()
        return withContext(coroutineDispatcher) {
            unSplashApiService.getPhotosOfCollection(
                id = query,
                page = page,
                perPage = perPage
            ).map { it.toPhotoItem() }
        }
    }

}