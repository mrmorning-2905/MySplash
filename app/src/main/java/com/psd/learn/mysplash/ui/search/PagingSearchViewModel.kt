package com.psd.learn.mysplash.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.psd.learn.mysplash.SEARCH_COLLECTIONS_TYPE
import com.psd.learn.mysplash.SEARCH_PHOTOS_TYPE
import com.psd.learn.mysplash.SEARCH_USERS_TYPE
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PagingSearchViewModel @Inject constructor(
    private val pagingRepository: UnSplashPagingRepository,
    photosLocalRepository: PhotosLocalRepository
) : ViewModel() {


    private val actionSharedFlow = MutableSharedFlow<SearchAction>(replay = 1)

    private val _resultStateFlow = MutableStateFlow(ResultSearchState())
    val resultStateFlow = _resultStateFlow.asStateFlow()

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

    val uiState: StateFlow<SearchUiState> = combine(searchAction, scrollAction, ::Pair)
        .map { (search, scroll) ->
            SearchUiState(query = search.query, hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = SearchUiState()
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchPhotoPagingData = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<PhotoItem>(search.query, SEARCH_PHOTOS_TYPE) { totalPhotos ->
                updateSearchResultState(SEARCH_PHOTOS_TYPE, totalPhotos)
            }
        }
        .cachedIn(viewModelScope)
        .combine(photosLocalRepository.getPhotoIdsStream()) { pagingData, localIdList ->
            pagingData.map { photoItem ->
                val isFavorite = localIdList.contains(photoItem.photoId)
                photoItem.copy(isFavorite = isFavorite)
            }
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(Dispatchers.Main)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchCollectionPagingData: Flow<PagingData<CollectionItem>> = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<CollectionItem>(search.query, SEARCH_COLLECTIONS_TYPE) { totalCollections ->
                updateSearchResultState(SEARCH_COLLECTIONS_TYPE, totalCollections)
            }
        }
        .cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchUserPagingData: Flow<PagingData<UserItem>> = searchAction
        .debounce(650L)
        .flatMapLatest { search ->
            pagingRepository.getSearchResultStream<UserItem>(search.query, SEARCH_USERS_TYPE) { totalUsers ->
                updateSearchResultState(SEARCH_USERS_TYPE, totalUsers)
            }
        }
        .cachedIn(viewModelScope)

    private fun updateSearchResultState(searchType: Int, totalResult: Int) {
        _resultStateFlow.update { result: ResultSearchState ->
            val resultMap = result.resultMap.toMutableMap()
            resultMap[searchType] = totalResult
            result.copy(resultMap = resultMap)
        }
    }
}

sealed class SearchAction {
    data class Search(val query: String?) : SearchAction()
    data class Scroll(val currentQuery: String?) : SearchAction()
}

data class SearchUiState(
    val query: String? = "",
    val hasNotScrolledForCurrentSearch: Boolean = false
)

data class ResultSearchState(
    val resultMap: Map<Int, Int> = HashMap()
)