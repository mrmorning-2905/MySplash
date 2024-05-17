package com.psd.learn.mysplash.ui.viewmodels

import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.remote.entity.CollectionResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService

class FeedCollectionsViewModel(
    private val unSplashApiService: UnSplashApiService
) : AbsListItemViewModel<CollectionItem>() {

    init {
        loadFirstPage()
    }

    override suspend fun getListItems(currentPage: Int, itemPerPage: Int): List<CollectionItem> {
        return unSplashApiService.getCollectionListOnFeed(currentPage, itemPerPage)
            .map { it.toCollectionItem() }
    }
}

private fun CollectionResponseItem.toCollectionItem(): CollectionItem {
    return CollectionItem(
        id = id,
        userName = user.name,
        userProfileUrl = user.profileImage.medium,
        coverPhotoUrl = coverPhoto.urls.regular,
        coverDescription = title,
        numberImages = totalPhotos,
        userId = user.id
    )
}