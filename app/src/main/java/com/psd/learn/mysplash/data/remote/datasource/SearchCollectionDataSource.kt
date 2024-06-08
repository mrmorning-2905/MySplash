package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.remote.entity.SearchCollectionResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchCollectionDataSource (
    private val unSplashApiService: UnSplashApiService,
    queryText: String?
) : AbsPagingDataSource<CollectionItem>(queryText) {

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

        return response.results.map { it.toCollectionItem() }
    }
}

private fun SearchCollectionResponseItem.Result.toCollectionItem(): CollectionItem {
    return CollectionItem(
        collectionId = id,
        userName = user.name,
        userProfileUrl = user.profileImage.medium,
        coverPhotoUrl = coverPhoto.urls.regular,
        coverDescription = title,
        numberImages = totalPhotos,
        userId = user.id,
        coverThumbnailUrl = coverPhoto.urls.thumb,
        coverColor = coverPhoto.color
    )
}