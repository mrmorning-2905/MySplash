package com.psd.learn.mysplash.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PagingFeedViewModel @Inject constructor(
    pagingRepository: UnSplashPagingRepository,
    photosLocalRepo: PhotosLocalRepository
) : ViewModel() {

    val collectionPagingDataFlow: Flow<PagingData<CollectionItem>> = pagingRepository
        .getFeedCollectionsStream()
        .cachedIn(viewModelScope)

    val photoPagingDataFlow: Flow<PagingData<PhotoItem>> = pagingRepository
        .getFeedPhotosStream()
        .cachedIn(viewModelScope)

    val favoritePhotoFlow: Flow<PagingData<PhotoItem>> = photosLocalRepo
        .getFavoritePhotosStream()
        .cachedIn(viewModelScope)

}

