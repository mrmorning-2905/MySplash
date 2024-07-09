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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photoDetailsDataSource: PhotoDetailsDataSource,
    private val photoLocalRepo: PhotosLocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val photoId = PhotoDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).photoId

    val photoDetailsResult: StateFlow<ResultState> =
        flow {
            try {
                val photoItem = photoDetailsDataSource.getPhoto(photoId)
                val localId = photoLocalRepo.observerPhotoId(photoId).first()
                Log.d("sangpd", "localId first: $localId")
                emit(ResultState.Success(data = photoItem.copy(isFavorite = localId != null)))
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
}