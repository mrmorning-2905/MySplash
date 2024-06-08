package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.entity.SearchPhotoResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchPhotoDataSource(
    private val unSplashApiService: UnSplashApiService,
    queryText: String?
) : AbsPagingDataSource<PhotoItem>(queryText) {

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

        return response.results.map { it.toPhotoItem() }
    }

}

private fun SearchPhotoResponseItem.Result.toPhotoItem(): PhotoItem {
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