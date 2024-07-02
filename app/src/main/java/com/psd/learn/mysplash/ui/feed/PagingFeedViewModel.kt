package com.psd.learn.mysplash.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingFeedViewModel @Inject constructor(
    pagingRepository: UnSplashPagingRepository,
    private val photosLocalRepo: PhotosLocalRepository
) : ViewModel() {

    private val favoriteActionStateFlow = MutableStateFlow<List<FavoriteAction>>(emptyList())

    val collectionPagingDataFlow: Flow<PagingData<CollectionItem>> = pagingRepository
        .getFeedCollectionsStream()
        .cachedIn(viewModelScope)

    val favoritePhotoFlow: Flow<PagingData<PhotoItem>> = photosLocalRepo
        .getFavoritePhotosStream()
        .cachedIn(viewModelScope)

    val photoPagingDataFlow: Flow<PagingData<PhotoItem>> = pagingRepository
        .getFeedPhotosStream()
        .map { pagingData: PagingData<PhotoItem> ->
            pagingData.map {
                val isFavorite = photosLocalRepo.checkFavoritePhotoById(it.photoId)
                if (isFavorite) it.copy(isFavorite = true)
                else it
            }
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)
        .combine(favoriteActionStateFlow) { pagingData, actions: List<FavoriteAction> ->
            actions.fold(pagingData) { acc: PagingData<PhotoItem>, event: FavoriteAction ->
                applyEvent(acc, event)
            }
        }

    fun addOrRemoveFavorite(photoItem: PhotoItem, status: Boolean) {
        viewModelScope.launch {
            val action: FavoriteAction
            if (status) {
                action = FavoriteAction.RemoveFavorite(photoItem)
                photosLocalRepo.removeFavoritePhoto(photoItem)
            } else {
                action = FavoriteAction.AddFavorite(photoItem)
                photosLocalRepo.addFavoritePhoto(photoItem.copy(isFavorite = true))
            }
            onFavoriteAction(action)
        }
    }


    fun onFavoriteAction(action: FavoriteAction) {
        favoriteActionStateFlow.value += action
    }
    
    private fun applyEvent(
        pagingData: PagingData<PhotoItem>,
        action: FavoriteAction
    ) : PagingData<PhotoItem> {
        return when(action) {
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

}

sealed class FavoriteAction {
    data class AddFavorite(val data: PhotoItem) : FavoriteAction()
    data class RemoveFavorite(val data: PhotoItem) : FavoriteAction()
}

