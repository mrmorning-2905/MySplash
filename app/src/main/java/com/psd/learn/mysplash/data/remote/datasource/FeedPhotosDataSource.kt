package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.entity.PhotoResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedPhotosDataSource(
    private val unSplashApiService: UnSplashApiService,
) : AbsPagingDataSource<PhotoItem>() {

    override val TAG: String
        get() = FeedPhotosDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        queryText: String?,
        page: Int,
        perPage: Int
    ): List<PhotoItem> {
        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getPhotoListOnFeed(page, perPage)
        }
        return response.map { it.toPhotoItem() }
    }
}

fun PhotoResponseItem.toPhotoItem(): PhotoItem {
    return PhotoItem(
        photoId = id,
        userName = user.name,
        userProfileUrl = user.profileImage.medium,
        coverPhotoUrl = urls.regular,
        photoDescription = altDescription ?: "",
        numberLikes = likes,
        userId = user.id,
        coverThumbnailUrl = urls.thumb,
        coverColor = color
    )
}