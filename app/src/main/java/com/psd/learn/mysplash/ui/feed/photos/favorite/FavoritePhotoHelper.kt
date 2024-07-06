package com.psd.learn.mysplash.ui.feed.photos.favorite

import androidx.paging.PagingData
import androidx.paging.map
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem

object FavoritePhotoHelper {

    private val resultListListener: ArrayList<AddOrRemoveFavoriteResult> = ArrayList()
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
    fun addResultListener(result: AddOrRemoveFavoriteResult) {
        resultListListener.add(result)
    }

    fun removeResultListener(result: AddOrRemoveFavoriteResult) {
        resultListListener.remove(result)
    }

    private fun notifyResult(currentStatus: Boolean, photoItem: PhotoItem) {
        resultListListener.forEach {
            it.updateFavorite(currentStatus, photoItem)
        }
    }

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
        notifyResult(!currentState, photoItem)
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

}

sealed class FavoriteAction {
    data class AddFavorite(val data: PhotoItem) : FavoriteAction()
    data class RemoveFavorite(val data: PhotoItem) : FavoriteAction()
}

interface AddOrRemoveFavoriteResult {
    fun updateFavorite(currentState: Boolean, photoItem: PhotoItem)
}