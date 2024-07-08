package com.psd.learn.mysplash.ui.feed.photos.favorite

import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem

object FavoritePhotoHelper {
    suspend fun executeAddOrRemoveFavorite(
        localRepo: PhotosLocalRepository,
        photoItem: PhotoItem,
        currentState: Boolean
    ) {
        if (currentState) {
            localRepo.removeFavoritePhoto(photoItem)
        } else {
            localRepo.addFavoritePhoto(photoItem.copy(isFavorite = true))
        }
    }

}