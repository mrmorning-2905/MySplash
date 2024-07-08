package com.psd.learn.mysplash.ui.feed.collections.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotoHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(
    private val pagingRepository: UnSplashPagingRepository,
    private val photosLocalRepository: PhotosLocalRepository
) : ViewModel() {

    private val collectionIdSharedFlow = MutableSharedFlow<String>(replay = 1)

    fun setCollectionId(collectionId: String) {
        viewModelScope.launch {
            collectionIdSharedFlow.emit(collectionId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val collectionPhotos = collectionIdSharedFlow
        .distinctUntilChanged()
        .flatMapLatest { collectionId ->
            pagingRepository.getCollectionPhotosStream(collectionId)
        }
        .cachedIn(viewModelScope)
        .combine(photosLocalRepository.getPhotoIdsStream()) { pagingData, localIdList ->
            Log.d("sangpd", "localIdList: $localIdList")
            pagingData.map { photoItem ->
                val isFavorite = localIdList.contains(photoItem.photoId)
                photoItem.copy(isFavorite = isFavorite)
            }
        }
        .asLiveData()

    fun addOrRemoveFavoriteFromCollectionDetails(currentState: Boolean, photoItem: PhotoItem) {
        viewModelScope.launch {
            FavoritePhotoHelper.executeAddOrRemoveFavorite(photosLocalRepository, photoItem, currentState)
        }
    }
}