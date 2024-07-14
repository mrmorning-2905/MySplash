package com.psd.learn.mysplash.ui.feed.photos.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.datasource.PhotoDetailsDataSource
import com.psd.learn.mysplash.ui.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photoDetailsDataSource: PhotoDetailsDataSource,
    private val photoLocalRepo: PhotosLocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val photoId = PhotoDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).photoId

    val photoDetailsResult: StateFlow<ResultState<PhotoItem>> =
        flow {
            photoDetailsDataSource.getResultPhoto(photoId)
                .onFailure { error -> Log.d("sangpd", "PhotoDetailsViewModel_getResultPhoto() failed: $error") }
                .fold(
                    onSuccess = {photoItem -> emit(ResultState.Success(data = photoItem))},
                    onFailure = {error -> emit(ResultState.Error(error))}
                )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResultState.Loading
            )

    fun observerLocalPhotoById() = photoLocalRepo.observerPhotoId(photoId)
}