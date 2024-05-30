package com.psd.learn.mysplash.ui.feed.photos

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.FeedPhotosFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.viewmodels.FeedPhotosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosListFragment :
    BaseListFragment<PhotoItem, FeedPhotosFragmentLayoutBinding>(inflate = FeedPhotosFragmentLayoutBinding::inflate) {

    private val viewModel by viewModels<FeedPhotosViewModel>()

    private val photosListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PhotosListAdapter(
            requestManager = Glide.with(this@PhotosListFragment),
            onItemClickListener = { photoId -> showMessageToast("clicked on photo have id: $photoId") },
            onProfileClickListener = { userId -> showMessageToast("clicked on user profile have id: $userId") }
        )
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun submitList(items: List<PhotoItem>) {
        photosListAdapter.submitList(items)
    }

    override fun setupView() {
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