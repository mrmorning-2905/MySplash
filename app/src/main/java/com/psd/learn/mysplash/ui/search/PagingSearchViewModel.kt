package com.psd.learn.mysplash.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    val userAction: (UiAction) -> Unit = { action -> viewModelScope.launch { actionSharedFlow.emit(action) } }

    val uiState: StateFlow<PagingUiState>
        get() {
            return combine(searchAction, scrollAction, ::Pair)
                .map { (search, scroll) ->
                    PagingUiState(query = search.query, hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery)
                }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000), initialValue = PagingUiState())
        }

    @Suppress("UNCHECKED_CAST")
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun <Item : Any> getSearchResult(searchType: Int): Flow<PagingData<Item>> {
        val result: Flow<PagingData<Item>> = searchAction
            .debounce(650L)
            .flatMapLatest {
                val queryText = it.query
                when(searchType) {
                    SEARCH_PHOTOS_TYPE -> pagingRepository.getSearchPhotoResultStream(queryText)
                    SEARCH_COLLECTIONS_TYPE -> pagingRepository.getSearchCollectionsResultStream(queryText)
                    SEARCH_USERS_TYPE -> pagingRepository.getSearchUsersResultStream(queryText)
                    else -> error("UnKnown")
                } as Flow<PagingData<Item>>
            }
            .cachedIn(viewModelScope)
        return result
    }

    companion object {
        const val SEARCH_PHOTOS_TYPE = 0
        const val SEARCH_COLLECTIONS_TYPE = 1
        const val SEARCH_USERS_TYPE = 2
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