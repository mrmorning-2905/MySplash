package com.psd.learn.mysplash.ui.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class PagingFeedViewModel @Inject constructor(
    pagingRepository: UnSplashPagingRepository,
    photosLocalRepo: PhotosLocalRepository
) : ViewModel() {

    val collectionPagingDataFlow: Flow<PagingData<CollectionItem>> = pagingRepository
        .getFeedCollectionsStream()
        .cachedIn(viewModelScope)

    val favoritePhotoFlow: Flow<PagingData<PhotoItem>> = photosLocalRepo
        .getFavoritePhotosStream()
        .cachedIn(viewModelScope)

    val photoPagingFlow = pagingRepository
        .getFeedPhotosStream()
        .cachedIn(viewModelScope)
        .combine(photosLocalRepo.observerLocalPhotoIdsStream()) { pagingData, localIdList: Result<List<String>> ->
            pagingData.map { photoItem ->
                localIdList
                    .onFailure { error -> Log.d("sangpd", "PagingFeedViewModel_observerLocalPhotoIds failed error: $error") }
                    .fold(
                        onSuccess = { idList ->
                            val isFavorite = idList.contains(photoItem.photoId)
                            photoItem.copy(isFavorite = isFavorite)
                        },
                        onFailure = { photoItem }
                    )
            }
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(Dispatchers.Main)
}

