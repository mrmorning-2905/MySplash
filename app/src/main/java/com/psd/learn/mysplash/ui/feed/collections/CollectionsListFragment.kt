package com.psd.learn.mysplash.ui.feed.collections

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.ViewModelFactory
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.FeedCollectionsFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.viewmodels.FeedCollectionsViewModel

class CollectionsListFragment : BaseListFragment<CollectionItem, FeedCollectionsFragmentLayoutBinding>(inflate = FeedCollectionsFragmentLayoutBinding::inflate) {

    private val viewModel by viewModels<FeedCollectionsViewModel> { ViewModelFactory }

    private val collectionListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CollectionsListAdapter(
            requestManager = Glide.with(this@CollectionsListFragment),
            onItemClickListener = { photoId -> showMessageToast("clicked on collection have id: $photoId") },
            onProfileClickListener = { userId -> showMessageToast("clicked on user profile have id: $userId") }
        )
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun submitList(items: List<CollectionItem>) {
        collectionListAdapter.submitList(items)
    }

    override fun setupView() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = collectionListAdapter
        }
    }

    override fun setupViewModel() {
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) {
            renderUiState(it, binding.progressBar)
        }
        handleLoadMorePage("", binding.recyclerView, viewModel)
    }

    companion object {
        fun newInstance() = CollectionsListFragment()
    }
}