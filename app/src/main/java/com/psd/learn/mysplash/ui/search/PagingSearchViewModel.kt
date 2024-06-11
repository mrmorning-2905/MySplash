package com.psd.learn.mysplash.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.psd.learn.mysplash.SEARCH_COLLECTIONS_TYPE
import com.psd.learn.mysplash.SEARCH_PHOTOS_TYPE
import com.psd.learn.mysplash.SEARCH_USERS_TYPE
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.utils.log.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PagingSearchViewModel @Inject constructor(
    private val pagingRepository: UnSplashPagingRepository
) : ViewModel() {

    private val TAG = PagingSearchViewModel::class.java.simpleName

    private val actionSharedFlow = MutableSharedFlow<UiAction>()

    private val searchAction = actionSharedFlow
        .filterIsInstance<UiAction.Search>()
        .distinctUntilChanged()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            replay = 1
        )

    private val scrollAction = actionSharedFlow
        .filterIsInstance<UiAction.Scroll>()
        .distinctUntilChanged()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            replay = 1
        )

    fun onApplyUserAction(uiAction: UiAction) {
        viewModelScope.launch {
            actionSharedFlow.emit(uiAction)
        }
    }

    val uiState: StateFlow<PagingUiState>
        get() {
            return combine(searchAction, scrollAction, ::Pair)
                .map { (search, scroll) ->
                    PagingUiState(query = search.query, hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery)
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                    initialValue = PagingUiState()
                )
        }

    private val _searchPhotoTotal = MutableSharedFlow<Int>()
    private val _searchCollectionTotal = MutableSharedFlow<Int>()
    private val _searchUserTotal = MutableSharedFlow<Int>()

    val searchPhotoTotal = _searchPhotoTotal.asSharedFlow()
    val searchCollectionTotal = _searchCollectionTotal.asSharedFlow()
    val searchUserTotal = _searchUserTotal.asSharedFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchPhotoPagingData: Flow<PagingData<PhotoItem>> = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<PhotoItem>(search.query, SEARCH_PHOTOS_TYPE) { resultFlow ->
                viewModelScope.launch {
                    _searchPhotoTotal.emitAll(resultFlow)
                }

            }
        }
        .cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchCollectionPagingData: Flow<PagingData<CollectionItem>> = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<CollectionItem>(search.query, SEARCH_COLLECTIONS_TYPE) { resultFlow ->
                viewModelScope.launch {
                    _searchCollectionTotal.emitAll(resultFlow)
                }
            }
        }
        .cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchUserPagingData: Flow<PagingData<UserItem>> = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<UserItem>(search.query, SEARCH_USERS_TYPE) { resultFlow ->
                viewModelScope.launch {
                    _searchUserTotal.emitAll(resultFlow)
                }

            }
        }
        .cachedIn(viewModelScope)
}

sealed class UiAction {
    data class Search(val query: String?) : UiAction()
    data class Scroll(val currentQuery: String?) : UiAction()
}

data class PagingUiState(
    val query: String? = "",
    val hasNotScrolledForCurrentSearch: Boolean = false
)