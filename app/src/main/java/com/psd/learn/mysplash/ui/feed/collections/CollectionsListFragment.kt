package com.psd.learn.mysplash.ui.feed.collections

import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.FeedCollectionsFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsListFragment : BaseListFragment<CollectionItem, FeedCollectionsFragmentLayoutBinding>(inflate = FeedCollectionsFragmentLayoutBinding::inflate) {

    private val viewModel by viewModels<FeedCollectionsViewModel>()

    private val collectionListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CollectionsListAdapter(
            requestManager = Glide.with(this@CollectionsListFragment),
            itemClickListener = mItemClickListener
        )
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