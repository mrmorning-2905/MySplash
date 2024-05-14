package com.psd.learn.mysplash.ui.core

sealed interface UiState<out T> {
    data object FirstPageLoading : UiState<Nothing>
    data object FirstPageError : UiState<Nothing>
    data class Content<T>(
        val items: List<T>,
        val currentPage: Int,
        val nextPageState: NextPageState
    ) : UiState<T>

    sealed interface NextPageState {
        data object Loading : NextPageState
        data object Idle : NextPageState
        data object Error : NextPageState
        data object Done : NextPageState
    }
}