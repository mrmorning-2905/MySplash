package com.psd.learn.mysplash.ui.feed.collections.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(
    pagingRepository: UnSplashPagingRepository,
    photosLocalRepository: PhotosLocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val collectionId = CollectionDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).collectionInfo.collectionId

    val collectionPhotos = pagingRepository
        .getCollectionPhotosStream(collectionId)
        .cachedIn(viewModelScope)
        .combine(photosLocalRepository.getPhotoIdsStream()) { pagingData, localIdList ->
            pagingData.map { photoItem ->
                val isFavorite = localIdList.contains(photoItem.photoId)
                photoItem.copy(isFavorite = isFavorite)
            }
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(Dispatchers.Main)
}