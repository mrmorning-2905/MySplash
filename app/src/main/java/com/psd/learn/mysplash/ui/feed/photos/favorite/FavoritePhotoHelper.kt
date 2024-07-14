package com.psd.learn.mysplash.ui.feed.photos.favorite

import android.content.Context
import android.util.Log
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

object FavoritePhotoHelper {
    suspend fun executeAddOrRemoveFavorite(
        context: Context,
        photoItem: PhotoItem
    ) {
        val entryPoint: FavoriteEntryPoint =
            EntryPointAccessors.fromApplication<FavoriteEntryPoint>(context)
        val localRepo = entryPoint.localRepo
        if (photoItem.isFavorite) {
            localRepo.removeFavoritePhoto(photoItem)
                .fold(
                    onSuccess = {},
                    onFailure = { error ->
                        Log.d(
                            "sangpd",
                            "executeAddOrRemoveFavorite_failed to removeFavoritePhoto id= ${photoItem.photoId}_ error: $error"
                        )
                    }
                )
        } else {
            localRepo.addFavoritePhoto(photoItem.copy(isFavorite = true))
                .fold(
                    onSuccess = {},
                    onFailure = { error ->
                        Log.d(
                            "sangpd",
                            "executeAddOrRemoveFavorite_failed to addFavoritePhoto id= ${photoItem.photoId}_ error: $error"
                        )
                    }
                )
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface FavoriteEntryPoint {
    val localRepo: PhotosLocalRepository
}