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
import com.psd.learn.mysplash.ui.feed.photos.FavoriteAction
import com.psd.learn.mysplash.ui.feed.photos.FavoritePhotoHelper
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

    val photoPagingDataFlow = pagingRepository
        .getFeedPhotosStream()
        .map { pagingData: PagingData<PhotoItem> ->
            FavoritePhotoHelper.mappingFavoriteFromLocal(photosLocalRepo, pagingData)
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)
        .combine(favoriteActionStateFlow) { pagingData, actions ->
            actions.fold(pagingData) { acc, event ->
                FavoritePhotoHelper.applyEvent(acc, event)
            }
        }
        .asLiveData()

    fun addOrRemoveFavoriteFromFeed(currentState: Boolean, photoItem: PhotoItem) {
        Log.d("sangpd", "addOrRemoveFavorite-isFavorite: ${photoItem.isFavorite}")
        viewModelScope.launch {
            FavoritePhotoHelper.executeAddOrRemoveFavorite(photosLocalRepo, photoItem, currentState, favoriteActionStateFlow)
        }
    }
}

