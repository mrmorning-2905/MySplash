package com.psd.learn.mysplash.ui.feed

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PagingFeedViewModel @Inject constructor(
    pagingRepository: UnSplashPagingRepository
) : ViewModel() {

    val collectionPagingDataFlow: Flow<PagingData<CollectionItem>> = pagingRepository.getFeedCollectionsStream()

    val photoPagingDataFlow: Flow<PagingData<PhotoItem>> = pagingRepository.getFeedPhotosStream()

}

