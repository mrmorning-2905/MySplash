package com.psd.learn.mysplash.ui.viewmodels

import com.psd.learn.mysplash.data.remote.entity.PhotoResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.data.local.entity.PhotoItem

class FeedPhotosViewModel(
    private val unSplashApiService: UnSplashApiService
) : AbsListItemViewModel<PhotoItem>() {
    init {
        loadFirstPage()
    }

    override suspend fun getListItems(currentPage: Int, itemPerPage: Int): List<PhotoItem> {
        return unSplashApiService.getPhotoListOnFeed(currentPage, itemPerPage)
            .map { it.toPhotoItem() }
    }
}

private fun PhotoResponseItem.toPhotoItem(): PhotoItem {
    return PhotoItem(
        id = id,
        userName = user.name,
        userProfileUrl = user.profileImage.medium,
        coverPhotoUrl = urls.regular,
        photoDescription = altDescription ?: "",
        numberLikes = likes,
        userId = user.id
    )
}