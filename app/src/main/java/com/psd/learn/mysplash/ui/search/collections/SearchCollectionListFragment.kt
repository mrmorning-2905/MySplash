package com.psd.learn.mysplash.ui.search.collections

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
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
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.feed.collections.CollectionsListAdapter
import com.psd.learn.mysplash.ui.feed.collections.CollectionsListFragment
import com.psd.learn.mysplash.ui.utils.debounce
import com.psd.learn.mysplash.ui.viewmodels.SearchCollectionViewModel

class SearchCollectionListFragment : BaseListFragment<CollectionItem, SearchCollectionFragmentLayoutBinding>(inflate = SearchCollectionFragmentLayoutBinding::inflate) {

    private val viewModel by activityViewModels<SearchCollectionViewModel> { ViewModelFactory }

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

    companion object{
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
        viewModel.queryLiveData
            .debounce(650L, viewModel.viewModelScope)
            .distinctUntilChanged()
            .observe(viewLifecycleOwner) {queryText ->
                viewModel.loadFirstPage(queryText)
                handleLoadMorePage(queryText, binding.recyclerView, viewModel)
            }

        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { uiState ->
            renderUiState(uiState, binding.progressBar)
        }

        viewModel.result.observe(viewLifecycleOwner) {totalResult ->
            binding.searchResult.text = "Result: $totalResult images"

        }
    }

    override fun onDestroyView() {
        Log.d("sangpd", "onDestroyView: SearchCollectionListFragment")
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }
}