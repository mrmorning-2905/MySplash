package com.psd.learn.mysplash.ui.feed.photos

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.entity.PhotoResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.AbsListItemViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedPhotosViewModel @Inject constructor(
    private val unSplashApiService: UnSplashApiService
) : AbsListItemViewModel<PhotoItem>() {
    init {
        loadFirstPage("")
    }

    override suspend fun getListItems(searchText: String, currentPage: Int, itemPerPage: Int): List<PhotoItem> {
        return unSplashApiService.getPhotoListOnFeed(currentPage, itemPerPage)
            .map { it.toPhotoItem() }
    }
}

private fun PhotoResponseItem.toPhotoItem(): PhotoItem {
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