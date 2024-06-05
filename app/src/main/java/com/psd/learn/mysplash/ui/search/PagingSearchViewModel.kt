package com.psd.learn.mysplash.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class PagingSearchViewModel<Item: Any> : ViewModel() {

    private val actionSharedFlow = MutableSharedFlow<UiAction>()

    private val searchAction: Flow<UiAction.Search> = actionSharedFlow
        .filterIsInstance<UiAction.Search>()
        .distinctUntilChanged()

    private val scrollAction = actionSharedFlow
        .filterIsInstance<UiAction.Scroll>()
        .distinctUntilChanged()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            replay = 1
        )

    abstract fun getSearchItems(query: String?): Flow<PagingData<Item>>

    val userAction: (UiAction) -> Unit = { action -> viewModelScope.launch { actionSharedFlow.emit(action) } }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow: Flow<PagingData<Item>> = searchAction
        .flatMapLatest { getSearchItems(it.query) }
        .cachedIn(viewModelScope)

    val uiState: StateFlow<PagingUiState>
        get() {
            return combine(searchAction, scrollAction, ::Pair)
                .map { (search, scroll) ->
                    PagingUiState(query = search.query, hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery)
                }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000), initialValue = PagingUiState())
        }
}

sealed class UiAction {
    data class Search(val query: String?) : UiAction()
    data class Scroll(val currentQuery: String?) : UiAction()
}

data class PagingUiState(
    val query: String? = "",
    val hasNotScrolledForCurrentSearch: Boolean = false
)