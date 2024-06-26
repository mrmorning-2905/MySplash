package com.psd.learn.mysplash.ui.feed.photos.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psd.learn.mysplash.data.remote.datasource.PhotoDetailsDataSource
import com.psd.learn.mysplash.ui.utils.ResultState
import com.psd.learn.mysplash.utils.log.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photoDetailsDataSource: PhotoDetailsDataSource
) : ViewModel() {

    private val TAG = PhotoDetailsDataSource::class.java.simpleName

    private val photoIdSharedFlow = MutableSharedFlow<String>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val photoDetailsResult: StateFlow<ResultState> = photoIdSharedFlow
        .distinctUntilChanged()
        .flatMapLatest { id ->
            flow {
                try {
                    val photoItem = photoDetailsDataSource.getPhoto(id)
                    emit(ResultState.Success(data = photoItem))
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Logger.e(TAG, "failed to load photo details: $e")
                    emit(ResultState.Error(e))
                }

            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ResultState.Loading
        )


    fun emitPhotoId(id: String) {
        viewModelScope.launch {
            photoIdSharedFlow.emit(id)
        }
    }
}