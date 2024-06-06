package com.psd.learn.mysplash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psd.learn.mysplash.ui.core.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

abstract class AbsListItemViewModel<T> : ViewModel() {
    private val _uiState = MutableLiveData<UiState<T>>(UiState.FirstPageIdle)

    internal val uiStateLiveData: LiveData<UiState<T>>
        get() = _uiState

    abstract suspend fun getListItems(searchText: String, currentPage: Int, itemPerPage: Int): List<T>

    fun loadFirstPage(searchText: String) {
        viewModelScope.launch {
            _uiState.value = UiState.FirstPageLoading
            try {
                val items = getListItems(searchText, currentPage = 1, itemPerPage = PER_PAGE )
                _uiState.value = UiState.Content(
                    items = items,
                    currentPage = 1,
                    nextPageState = UiState.NextPageState.Idle
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Log.d("sangpd", "loadFirstPage: $e")
                _uiState.value = UiState.FirstPageError
            }
        }
    }

    fun loadNextPage(searchText: String) {
        val state = _uiState.value!!
        if (state !is UiState.Content) return

        when (state.nextPageState) {
            UiState.NextPageState.Done -> return
            UiState.NextPageState.Error -> return
            UiState.NextPageState.Loading -> return
            UiState.NextPageState.Idle -> {
                _uiState.value = state.copy(nextPageState = UiState.NextPageState.Loading)
                viewModelScope.launch {
                    val nextPage = state.currentPage + 1
                    try {
                        val newItems = getListItems(searchText, currentPage = nextPage, itemPerPage = PER_PAGE)
                        _uiState.value = state.copy(
                            items = (state.items + newItems),
                            currentPage = nextPage,
                            nextPageState = if (newItems.size < PER_PAGE) {
                                UiState.NextPageState.Done
                            } else {
                                UiState.NextPageState.Idle
                            }
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Throwable) {
                        _uiState.value = state.copy(nextPageState = UiState.NextPageState.Error)
                    }
                }
            }
        }
    }

    companion object {
        const val PER_PAGE = 30
    }
}