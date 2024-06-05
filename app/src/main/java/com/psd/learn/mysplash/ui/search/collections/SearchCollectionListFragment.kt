package com.psd.learn.mysplash.ui.search.collections

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.SearchCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.core.UiState
import com.psd.learn.mysplash.ui.feed.collections.CollectionsListAdapter
import com.psd.learn.mysplash.ui.search.SearchViewModel
import com.psd.learn.mysplash.ui.utils.debounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCollectionListFragment :
    BaseListFragment<CollectionItem, SearchCollectionFragmentLayoutBinding>(inflate = SearchCollectionFragmentLayoutBinding::inflate) {
    private val mainSearchViewModel by activityViewModels<SearchViewModel>()
    private val searchCollectionViewModel by viewModels<SearchCollectionViewModel>()

    private val searchCollectionListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CollectionsListAdapter(
            requestManager = Glide.with(this@SearchCollectionListFragment),
            itemClickListener = mItemClickListener
        )
    }

    companion object {
        fun newInstance() = SearchCollectionListFragment()
    }

    override fun submitList(items: List<CollectionItem>) {
        searchCollectionListAdapter.submitList(items)
    }

    override fun setupView() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = searchCollectionListAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupViewModel() {
        mainSearchViewModel.queryLiveData
            .debounce(650L, searchCollectionViewModel.viewModelScope)
            .distinctUntilChanged()
            .observe(viewLifecycleOwner) { queryText ->
                if (queryText.isNotEmpty()) {
                    searchCollectionViewModel.loadFirstPage(queryText)
                    handleLoadMorePage(queryText, binding.recyclerView, searchCollectionViewModel)
                }
            }

        searchCollectionViewModel.uiStateLiveData.observe(viewLifecycleOwner) { uiState ->
            renderUiState(uiState, binding.progressBar)
            binding.searchResult.visibility = if (uiState is UiState.Content) View.VISIBLE else View.GONE
        }

        searchCollectionViewModel.result.observe(viewLifecycleOwner) { totalResult ->
            binding.searchResult.text = "Result: $totalResult collections"

        }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }
}