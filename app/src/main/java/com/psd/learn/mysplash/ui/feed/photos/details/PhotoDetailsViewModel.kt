package com.psd.learn.mysplash.ui.feed.photos.details

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.google.gson.Gson
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.datasource.PhotoDetailsDataSource
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotoHelper
import com.psd.learn.mysplash.ui.utils.ResultState
import com.psd.learn.mysplash.worker.DownloadWorker
import com.psd.learn.mysplash.worker.RequestInfo
import com.psd.learn.mysplash.worker.SingleSetWallpaperWorker
import com.psd.learn.mysplash.worker.SingleSetWallpaperWorker.Companion.SINGLE_WALLPAPER_WORKER_ID
import com.psd.learn.mysplash.worker.toDownloadInfoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photoDetailsDataSource: PhotoDetailsDataSource,
    photoLocalRepo: PhotosLocalRepository,
    private val gson: Gson,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val photoId = PhotoDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).photoId

    val photoDetailsResult: StateFlow<ResultState<PhotoItem>> =
        flow {
            photoDetailsDataSource.getResultPhoto(photoId)
                .onFailure { error ->
                    Log.d(
                        "sangpd",
                        "PhotoDetailsViewModel_getResultPhoto() failed: $error"
                    )
                }
                .fold(
                    onSuccess = { photoItem ->
                        emit(ResultState.Success(data = photoItem))
                    },
                    onFailure = { error -> emit(ResultState.Error(error)) }
                )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResultState.Loading
            )

    val observerLocalPhotoById: StateFlow<ResultState<String?>> =
        photoLocalRepo.observerPhotoId(photoId)
            .map { result ->
                result.fold(
                    onSuccess = { localId -> ResultState.Success(localId) },
                    onFailure = { error -> ResultState.Error(error) }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResultState.Loading
            )

    fun downloadPhoto(
        context: Context,
        photoItem: PhotoItem,
        lifecycle: Lifecycle
    ) {
        val downloadInfo = photoItem.toDownloadInfoItem()
        val requestInfo = RequestInfo(totalFiles = 1, listItem = listOf(downloadInfo))
        val workId = DownloadWorker.enQueueDownload(context, gson, requestInfo)

        viewModelScope.launch {
            val workQuery = WorkQuery.fromIds(workId)
            WorkManager.getInstance(context)
                .getWorkInfosFlow(workQuery)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collectLatest { workList ->
                    workList.getOrNull(0).let {
                        if (it?.state == WorkInfo.State.SUCCEEDED) {
                            Toast.makeText(
                                context,
                                "Download file completed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    fun setWallpaper(
        context: Context,
        photoItem: PhotoItem,
        lifecycle: Lifecycle
    ) {
        SingleSetWallpaperWorker.enqueueSingleSetWallPaperWork(context, gson, photoItem)
        viewModelScope.launch {
            val workQuery = WorkQuery.fromUniqueWorkNames(SINGLE_WALLPAPER_WORKER_ID)
            WorkManager.getInstance(context)
                .getWorkInfosFlow(workQuery)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collectLatest { workList ->
                    workList.getOrNull(0).let {
                        if (it?.state == WorkInfo.State.SUCCEEDED) {
                            Toast.makeText(
                                context,
                                "Wallpaper was setup",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }
}