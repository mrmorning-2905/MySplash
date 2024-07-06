package com.psd.learn.mysplash.ui.feed.collections.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoriteAction
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotoHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(
    private val pagingRepository: UnSplashPagingRepository,
    private val photosLocalRepository: PhotosLocalRepository
) : ViewModel() {

    private val collectionIdSharedFlow = MutableSharedFlow<String>(replay = 1)

    private val favoriteActionStateFlow = MutableStateFlow<List<FavoriteAction>>(emptyList())

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
        .map { pagingData: PagingData<PhotoItem> ->
            FavoritePhotoHelper.mappingFavoriteFromLocal(photosLocalRepository, pagingData)
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)
        .combine(favoriteActionStateFlow) { pagingData, actions ->
            actions.fold(pagingData) { acc, event ->
                FavoritePhotoHelper.applyEvent(acc, event)
            }
        }
        .asLiveData()

    fun addOrRemoveFavoriteFromCollectionDetails(currentState: Boolean, photoItem: PhotoItem) {
        viewModelScope.launch {
            FavoritePhotoHelper.executeAddOrRemoveFavorite(photosLocalRepository, photoItem, currentState, favoriteActionStateFlow)
        }
    }
}