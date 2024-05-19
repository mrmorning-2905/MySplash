package com.psd.learn.mysplash.ui.search.collections

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.ViewModelFactory
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.SearchCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.feed.collections.CollectionsListAdapter
import com.psd.learn.mysplash.ui.utils.debounce
import com.psd.learn.mysplash.ui.viewmodels.SearchCollectionViewModel
import com.psd.learn.mysplash.ui.viewmodels.SearchViewModel

class SearchCollectionListFragment :
    BaseListFragment<CollectionItem, SearchCollectionFragmentLayoutBinding>(inflate = SearchCollectionFragmentLayoutBinding::inflate) {
    private val mainSearchViewModel by activityViewModels<SearchViewModel> { ViewModelFactory }
    private val searchCollectionViewModel by viewModels<SearchCollectionViewModel> { ViewModelFactory }

    private val searchCollectionListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CollectionsListAdapter(
            requestManager = Glide.with(this@SearchCollectionListFragment),
            onItemClickListener = { photoId -> showMessageToast("clicked on collection have id: $photoId") },
            onProfileClickListener = { userId -> showMessageToast("clicked on user profile have id: $userId") }
        )
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
            layoutManager = LinearLayoutManager(context)
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
        }

        searchCollectionViewModel.result.observe(viewLifecycleOwner) { totalResult ->
            binding.searchResult.text = "Result: $totalResult collections"

        }
    }

    override fun onDestroyView() {
        Log.d("sangpd", "onDestroyView: SearchCollectionListFragment")
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }
}