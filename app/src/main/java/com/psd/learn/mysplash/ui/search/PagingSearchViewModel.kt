package com.psd.learn.mysplash.ui.search

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PagingSearchViewModel @Inject constructor(
    private val pagingRepository: UnSplashPagingRepository
) : ViewModel() {

    private val TAG = PagingSearchViewModel::class.java.simpleName

    private val actionSharedFlow = MutableSharedFlow<SearchAction>()

    private val searchAction = actionSharedFlow
        .filterIsInstance<SearchAction.Search>()
        .distinctUntilChanged()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            replay = 1
        )

    private val scrollAction = actionSharedFlow
        .filterIsInstance<SearchAction.Scroll>()
        .distinctUntilChanged()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            replay = 1
        )

    fun onApplyUserAction(searchAction: SearchAction) {
        viewModelScope.launch {
            actionSharedFlow.emit(searchAction)
        }
    }

    val uiState: StateFlow<SearchUiState>
        get() {
            return combine(searchAction, scrollAction, ::Pair)
                .map { (search, scroll) ->
                    SearchUiState(query = search.query, hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery)
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                    initialValue = SearchUiState()
                )
        }

    private val _searchPhotoTotal = MutableSharedFlow<Int>(replay = 1)
    private val _searchCollectionTotal = MutableSharedFlow<Int>(replay = 1)
    private val _searchUserTotal = MutableSharedFlow<Int>(replay = 1)

    val searchPhotoTotal = _searchPhotoTotal.asSharedFlow()
    val searchCollectionTotal = _searchCollectionTotal.asSharedFlow()
    val searchUserTotal = _searchUserTotal.asSharedFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchPhotoPagingData: Flow<PagingData<PhotoItem>> = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<PhotoItem>(search.query, SEARCH_PHOTOS_TYPE) {
                viewModelScope.launch {
                    _searchPhotoTotal.emit(it)
                }
            }
        }
        .cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchCollectionPagingData: Flow<PagingData<CollectionItem>> = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<CollectionItem>(search.query, SEARCH_COLLECTIONS_TYPE) {
                viewModelScope.launch {
                    _searchCollectionTotal.emit(it)
                }
            }
        }
        .cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchUserPagingData: Flow<PagingData<UserItem>> = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<UserItem>(search.query, SEARCH_USERS_TYPE) {
                viewModelScope.launch {
                    _searchUserTotal.emit(it)
                }
            }
        }
        .cachedIn(viewModelScope)
}

sealed class SearchAction {
    data class Search(val query: String?) : SearchAction()
    data class Scroll(val currentQuery: String?) : SearchAction()
}

data class SearchUiState(
    val query: String? = "",
    val hasNotScrolledForCurrentSearch: Boolean = false
)