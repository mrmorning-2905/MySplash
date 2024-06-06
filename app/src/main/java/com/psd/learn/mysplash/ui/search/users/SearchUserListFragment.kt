package com.psd.learn.mysplash.ui.search.users

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
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.SearchUserFragmentLayoutBinding
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
class SearchUserListFragment :
    BasePagingFragment<SearchUserFragmentLayoutBinding>(inflate = SearchUserFragmentLayoutBinding::inflate) {

    private val searchViewModel by activityViewModels<PagingSearchViewModel>()

    private val searUserListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SearchUserListAdapter(
            requestManager = Glide.with(this@SearchUserListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        val pagingData = searchViewModel.getSearchResult<UserItem>(PagingSearchViewModel.SEARCH_USERS_TYPE)
        Log.d("sangpd", "SearchUserListFragment_onViewCreated: $pagingData")
        initPagingData(
            pagingUiState = searchViewModel.uiState,
            pagingData = pagingData
        )
    }

    private fun initAdapter() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = searUserListAdapter
        }
    }

    private fun initPagingData(
        pagingUiState: StateFlow<PagingUiState>,
        pagingData: Flow<PagingData<UserItem>>
    ) {
        binding.recyclerView.addOnScrollListener( object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) {
                    val scrollAction = UiAction.Scroll(currentQuery = pagingUiState.value.query)
                    searchViewModel.onApplyUserAction(scrollAction)
                }
            }
        })

        val notLoading = searUserListAdapter
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
            pagingData.collectLatest(searUserListAdapter::submitData)
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
        fun newInstance() = SearchUserListFragment()
    }
}