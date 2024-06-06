package com.psd.learn.mysplash.ui.feed.photos

import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.FeedPhotosFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosListFragment :
    BaseListFragment<PhotoItem, FeedPhotosFragmentLayoutBinding>(inflate = FeedPhotosFragmentLayoutBinding::inflate) {

    private val viewModel by viewModels<FeedPhotosViewModel>()

    private val photosListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PhotosListAdapter(
            requestManager = Glide.with(this@PhotosListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override fun submitList(items: List<PhotoItem>) {
        photosListAdapter.submitList(items)
    }

    override fun initAdapter() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = photosListAdapter
        }
    }

    override fun setupViewModel() {
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) {
            renderUiState(it, binding.progressBar)
        }
        handleLoadMorePage("", binding.recyclerView, viewModel)
    }

    companion object {
        fun newInstance() = PhotosListFragment()
    }
}