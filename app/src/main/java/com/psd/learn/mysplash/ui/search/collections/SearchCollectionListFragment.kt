package com.psd.learn.mysplash.ui.search.collections

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.SearchCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import com.psd.learn.mysplash.ui.search.PagingUiState
import com.psd.learn.mysplash.ui.search.UiAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchCollectionListFragment :
    BasePagingFragment<SearchCollectionFragmentLayoutBinding>(inflate = SearchCollectionFragmentLayoutBinding::inflate) {

    private val searchViewModel by activityViewModels<PagingSearchViewModel>()

    private val searchCollectionAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SearchCollectionPagingAdapter(
            requestManager = Glide.with(this@SearchCollectionListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        val pagingData = searchViewModel.getSearchResult<CollectionItem>(PagingSearchViewModel.SEARCH_COLLECTIONS_TYPE)
        initPagingData(
            pagingUiState = searchViewModel.uiState,
            pagingData = pagingData,
            handleUiAction = searchViewModel.userAction)
    }

    private fun initAdapter() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = searchCollectionAdapter
        }
    }

    private fun initPagingData(
        pagingUiState: StateFlow<PagingUiState>,
        pagingData: Flow<PagingData<CollectionItem>>,
        handleUiAction: (UiAction.Scroll) -> Unit
    ) {
        binding.recyclerView.addOnScrollListener( object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) handleUiAction(UiAction.Scroll(currentQuery = pagingUiState.value.query))
            }
        })

        val notLoading = searchCollectionAdapter
            .loadStateFlow
            .distinctUntilChangedBy { it.source.refresh }
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrollForCurrentSearch = pagingUiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrollForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(searchCollectionAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) {
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = SearchCollectionListFragment()
    }
}