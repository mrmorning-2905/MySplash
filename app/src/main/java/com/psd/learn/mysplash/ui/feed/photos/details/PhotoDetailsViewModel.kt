package com.psd.learn.mysplash.ui.feed.photos.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.datasource.PhotoDetailsDataSource
import com.psd.learn.mysplash.ui.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
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
            try {
                val photoItem = photoDetailsDataSource.getPhoto(photoId)
                emit(ResultState.Success(data = photoItem))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emit(ResultState.Error(e))
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResultState.Loading
            )

    fun observerLocalPhotoById() = photoLocalRepo.observerPhotoId(photoId)
}