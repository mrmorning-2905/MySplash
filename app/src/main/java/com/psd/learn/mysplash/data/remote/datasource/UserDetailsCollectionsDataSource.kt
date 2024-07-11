package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserDetailsCollectionsDataSource(
    private val apiService: UnSplashApiService,
    private val dispatcher: CoroutineDispatcher,
    userNameAccount: String
) : AbsPagingDataSource<CollectionItem>(userNameAccount) {
    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<CollectionItem> {
        if (query == null) return emptyList()
        return withContext(dispatcher) {
            apiService.getUserCollections(
                userNameAccount = query,
                page = page,
                perPage = perPage
            ).map { it.toCollectionItem() }
        }
    }

}