package com.psd.learn.mysplash.ui.search.photos

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.SearchPhotoFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.core.UiState
import com.psd.learn.mysplash.ui.feed.photos.PhotosListAdapter
import com.psd.learn.mysplash.ui.search.SearchViewModel
import com.psd.learn.mysplash.ui.utils.debounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPhotoListFragment :
    BaseListFragment<PhotoItem, SearchPhotoFragmentLayoutBinding>(inflate = SearchPhotoFragmentLayoutBinding::inflate) {
    private val mainSearchViewModel by activityViewModels<SearchViewModel>()
    private val searchPhotoViewModel by viewModels<SearchPhotoViewModel>()
    private val searchPhotoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PhotosListAdapter(
            requestManager = Glide.with(this@SearchPhotoListFragment),
            itemClickListener = mItemClickListener
        )
    }

    companion object {
        fun newInstance() = SearchPhotoListFragment()
    }

    override fun submitList(items: List<PhotoItem>) {
        searchPhotoAdapter.submitList(items)
    }

    override fun setupView() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = searchPhotoAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupViewModel() {
        mainSearchViewModel.queryLiveData
            .debounce(650L, searchPhotoViewModel.viewModelScope)
            .distinctUntilChanged()
            .observe(viewLifecycleOwner) { queryText ->
                if (queryText.isNotEmpty()) {
                    searchPhotoViewModel.loadFirstPage(queryText)
                    handleLoadMorePage(queryText, binding.recyclerView, searchPhotoViewModel)
                }
            }

        searchPhotoViewModel.uiStateLiveData.observe(viewLifecycleOwner) { uiState ->
            renderUiState(uiState, binding.progressBar)
            binding.searchResult.visibility = if (uiState is UiState.Content) View.VISIBLE else View.GONE
        }

        searchPhotoViewModel.result.observe(viewLifecycleOwner) { totalResult ->
            binding.searchResult.text = "Result: $totalResult images"
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }
}