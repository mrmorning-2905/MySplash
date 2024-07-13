package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserDetailsPhotosDataSource(
    private val apiService: UnSplashApiService,
    private val dispatcher: CoroutineDispatcher,
    userNameAccount: String
) : AbsPagingDataSource<PhotoItem>(userNameAccount) {
    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<PhotoItem> {
        if (query == null) return emptyList()
        return withContext(dispatcher) {
            apiService.getUserPhotos(
                userNameAccount = query,
                page = page,
                perPage = perPage
            ).map { it.toPhotoItem() }
        }
    }

}