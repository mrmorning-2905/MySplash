package com.psd.learn.mysplash.ui.feed.photos

import androidx.paging.PagingData
import androidx.paging.map
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import kotlinx.coroutines.flow.MutableStateFlow

object FavoritePhotoHelper {
    fun applyEvent(
        pagingData: PagingData<PhotoItem>,
        action: FavoriteAction
    ): PagingData<PhotoItem> {
        return when (action) {
            is FavoriteAction.AddFavorite -> {
                pagingData.map {
                    if (action.data.photoId == it.photoId) return@map it.copy(isFavorite = true)
                    else return@map it

                }
            }

            is FavoriteAction.RemoveFavorite -> {
                pagingData.map {
                    if (action.data.photoId == it.photoId) return@map it.copy(isFavorite = false)
                    else return@map it
                }
            }
        }
    }

    suspend fun executeAddOrRemoveFavorite(
        localRepo: PhotosLocalRepository,
        photoItem: PhotoItem,
        currentState: Boolean,
        actionStateFlow: MutableStateFlow<List<FavoriteAction>>
    ) {
        if (currentState) {
            onFavoriteAction(actionStateFlow, FavoriteAction.RemoveFavorite(photoItem))
            localRepo.removeFavoritePhoto(photoItem)
        } else {
            onFavoriteAction(actionStateFlow, FavoriteAction.AddFavorite(photoItem))
            localRepo.addFavoritePhoto(photoItem.copy(isFavorite = true))
        }
    }

    fun mappingFavoriteFromLocal(
        localRepo: PhotosLocalRepository,
        pagingData: PagingData<PhotoItem>
    ): PagingData<PhotoItem> {
        return pagingData.map {
            val isFavorite = localRepo.checkFavoritePhotoById(it.photoId)
            if (isFavorite) it.copy(isFavorite = true)
            else it
        }
    }


    private fun onFavoriteAction(actionStateFlow: MutableStateFlow<List<FavoriteAction>>, action: FavoriteAction) {
        actionStateFlow.value += action
    }
}

sealed class FavoriteAction {
    data class AddFavorite(val data: PhotoItem) : FavoriteAction()
    data class RemoveFavorite(val data: PhotoItem) : FavoriteAction()
}