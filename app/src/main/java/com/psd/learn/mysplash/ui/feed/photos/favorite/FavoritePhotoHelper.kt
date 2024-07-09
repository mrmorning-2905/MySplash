package com.psd.learn.mysplash.ui.feed.photos.favorite

import android.content.Context
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

object FavoritePhotoHelper {
    suspend fun executeAddOrRemoveFavorite(
        context: Context,
        photoItem: PhotoItem,
        currentState: Boolean
    ) {
        val entryPoint: FavoriteEntryPoint = EntryPointAccessors.fromApplication<FavoriteEntryPoint>(context)
        val localRepo = entryPoint.localRepo
        if (currentState) {
            localRepo.removeFavoritePhoto(photoItem)
        } else {
            localRepo.addFavoritePhoto(photoItem.copy(isFavorite = true))
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface FavoriteEntryPoint {
    val localRepo: PhotosLocalRepository
}