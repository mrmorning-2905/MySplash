package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.remote.entity.CollectionResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedCollectionsDataSource(
    private val unSplashApiService: UnSplashApiService,
) : AbsPagingDataSource<CollectionItem>() {

    override val TAG: String
        get() = FeedCollectionsDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        queryText: String?,
        page: Int,
        perPage: Int
    ): List<CollectionItem> {
        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getCollectionListOnFeed(page, perPage)
        }
        return response.map { it.toCollectionItem() }
    }

}

private fun CollectionResponseItem.toCollectionItem(): CollectionItem {
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