package com.psd.learn.mysplash.ui.search.photos

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.ViewModelFactory
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.SearchPhotoFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.feed.photos.PhotosListAdapter
import com.psd.learn.mysplash.ui.utils.debounce
import com.psd.learn.mysplash.ui.viewmodels.SearchPhotoViewModel

class SearchPhotoListFragment : BaseListFragment<PhotoItem, SearchPhotoFragmentLayoutBinding>(inflate = SearchPhotoFragmentLayoutBinding::inflate) {
    private val viewModel by activityViewModels<SearchPhotoViewModel> { ViewModelFactory }
    private val searchPhotoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PhotosListAdapter(
            requestManager = Glide.with(this@SearchPhotoListFragment),
            onItemClickListener = { photoId -> showMessageToast("clicked on photo have id: $photoId") },
            onProfileClickListener = { userId -> showMessageToast("clicked on user profile have id: $userId") }
        )
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        fun newInstance() = SearchPhotoListFragment()
    }

    override fun submitList(items: List<PhotoItem>) {
        searchPhotoAdapter.submitList(items)
    }

    override fun setupView() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = searchPhotoAdapter
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
        Log.d("sangpd", "onDestroyView: SearchPhotoListFragment")
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }
}