package com.psd.learn.mysplash.ui.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.utils.log.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    val collectionPagingDataFlow: Flow<PagingData<CollectionItem>> = pagingRepository
        .getFeedCollectionsStream()
        .cachedIn(viewModelScope)

    val favoritePhotoFlow: Flow<PagingData<PhotoItem>> = photosLocalRepo
        .getFavoritePhotosStream()
        .cachedIn(viewModelScope)

    val photoPagingDataFlow: Flow<PagingData<PhotoItem>> = pagingRepository
        .getFeedPhotosStream()
        .cachedIn(viewModelScope)
        .combine(photosLocalRepo.getPhotoIdsStream()) { remotePagingPhotos, localPhotoIds ->
            remotePagingPhotos.map { photoItem ->
                val isFavorite = localPhotoIds.contains(photoItem.photoId)
                Logger.d("sangpd", "PagingFeedViewModel: isFavorite: $isFavorite")
                if (isFavorite) {
                    photoItem.copy(isFavorite = true)
                } else {
                    photoItem
                }
            }
        }
        .cachedIn(viewModelScope)

    fun insertFavoritePhoto(photoItem: PhotoItem) {
        viewModelScope.launch {
            photosLocalRepo.addFavoritePhoto(photoItem)
        }
    }

    fun removeFavoritePhoto(photoItem: PhotoItem) {
        viewModelScope.launch {
            photosLocalRepo.removeFavoritePhoto(photoItem)
        }
    }

}

