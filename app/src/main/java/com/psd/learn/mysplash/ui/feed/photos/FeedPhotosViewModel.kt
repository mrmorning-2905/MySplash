package com.psd.learn.mysplash.ui.feed.photos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psd.learn.mysplash.data.remote.entity.PhotoResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.ui.core.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class FeedPhotosViewModel(
    private val unSplashApiService: UnSplashApiService
) : /*ViewModel()*/AbsListItemViewModel<PhotoItem>() {
//    private val _uiState = MutableLiveData<UiState<PhotoItem>>(UiState.FirstPageLoading)
//
//    internal val uiStateLiveData: LiveData<UiState<PhotoItem>>
//        get() = _uiState
//
//    init {
//        loadFirstPage()
//    }

//    private fun loadFirstPage() {
//        viewModelScope.launch {
//            _uiState.value = UiState.FirstPageLoading
//            try {
//                val items = unSplashApiService.getPhotoListOnFeed(
//                    page = 1,
//                    perPage = PER_PAGE
//                ).map { it.toPhotoItem() }
//
//                _uiState.value = UiState.Content(
//                    items = items,
//                    currentPage = 1,
//                    nextPageState = UiState.NextPageState.Idle,
//                )
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Throwable) {
//                Log.d("sangpd", "loadFirstPage_error: $e")
//                _uiState.value = UiState.FirstPageError
//            }
//        }
//    }
//
//    fun loadNextPage() {
//        val state = _uiState.value!!
//        if (state !is UiState.Content) return
//
//        when (state.nextPageState) {
//            UiState.NextPageState.Done -> return
//            UiState.NextPageState.Error -> return
//            UiState.NextPageState.Loading -> return
//            UiState.NextPageState.Idle -> {
//                _uiState.value = state.copy(nextPageState = UiState.NextPageState.Loading)
//
//                viewModelScope.launch {
//                    val nextPage = state.currentPage + 1
//
//                    try {
//                        val newItems = unSplashApiService.getPhotoListOnFeed(
//                                page = nextPage,
//                                perPage = PER_PAGE,
//                            ).map { it.toPhotoItem() }
//
//                        _uiState.value = state.copy(
//                            items = (state.items + newItems).distinctBy { it.photoId },
//                            currentPage = nextPage,
//                            nextPageState = if (newItems.size < PER_PAGE) {
//                                UiState.NextPageState.Done
//                            } else {
//                                UiState.NextPageState.Idle
//                            }
//                        )
//                    } catch (e: CancellationException) {
//                        throw e
//                    } catch (e: Throwable) {
//                        _uiState.value = state.copy(nextPageState = UiState.NextPageState.Error)
//                    }
//                }
//            }
//        }
//    }

//    private companion object {
//        const val PER_PAGE = 30
//    }
    override suspend fun getListItems(currentPage: Int, itemPerPage: Int): List<PhotoItem> {
        val listItems =
            unSplashApiService.getPhotoListOnFeed(currentPage, itemPerPage).map { it.toPhotoItem() }
        Log.d(
            "sangpd",
            "getListItems_currentPage: $currentPage - intemPerPage: $itemPerPage - unSplashApiService: $unSplashApiService"
        )
        return listItems
    }
}

private fun PhotoResponseItem.toPhotoItem(): PhotoItem {
    return PhotoItem(
        photoId = id,
        userOwnerName = user.name,
        userProfileUrl = user.profileImage.medium,
        coverPhotoUrl = urls.regular
    )
}