package com.psd.learn.mysplash.ui.feed.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psd.learn.mysplash.data.remote.datasource.PhotoDetailsDataSource
import com.psd.learn.mysplash.ui.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photoDetailsDataSource: PhotoDetailsDataSource
) : ViewModel() {

    private val _photoDetailsResult = MutableSharedFlow<ResultState>()
    val photoDetailsResult = _photoDetailsResult.asSharedFlow()
    fun getPhotoDetailResult(photoId: String) {
        viewModelScope.launch {
            emitResult(ResultState.Loading)
            try {
                val photoItem = photoDetailsDataSource.getPhoto(photoId)
                emitResult(ResultState.Success(data = photoItem))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emitResult(ResultState.Error(e))
            }
        }
    }

    private fun emitResult(resultState: ResultState) {
        viewModelScope.launch {
            _photoDetailsResult.emit(resultState)
        }
    }
}