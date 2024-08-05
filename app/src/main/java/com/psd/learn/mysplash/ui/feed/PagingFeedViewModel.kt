package com.psd.learn.mysplash.ui.feed

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.google.gson.Gson
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.worker.DownloadWorker
import com.psd.learn.mysplash.worker.RequestInfo
import com.psd.learn.mysplash.worker.toDownloadInfoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingFeedViewModel @Inject constructor(
    pagingRepository: UnSplashPagingRepository,
    photosLocalRepo: PhotosLocalRepository,
    private val gson: Gson
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
                    .onFailure { error ->
                        Log.d(
                            "sangpd",
                            "PagingFeedViewModel_observerLocalPhotoIds failed error: $error"
                        )
                    }
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

    fun downloadCheckedFiles(
        context: Context,
        checkedList: List<PhotoItem>,
        lifecycle: Lifecycle,
        doOnSuccess: () -> Unit
    ) {
        val downloadList = checkedList.map { it.toDownloadInfoItem() }
        val requestInfo = RequestInfo(totalFiles = downloadList.size, listItem = downloadList)
        val workerId = DownloadWorker.enQueueDownload(context, gson, requestInfo)
        viewModelScope.launch {
            val workQuery = WorkQuery.fromIds(workerId)
            WorkManager.getInstance(context)
                .getWorkInfosFlow(workQuery)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collectLatest { workList ->
                    workList.getOrNull(0).let {
                        if (it?.state == WorkInfo.State.SUCCEEDED) {
                            Toast.makeText(
                                context,
                                "Download checked files completed",
                                Toast.LENGTH_SHORT
                            ).show()
                            doOnSuccess()
                        }
                    }
                }
        }
    }
}

