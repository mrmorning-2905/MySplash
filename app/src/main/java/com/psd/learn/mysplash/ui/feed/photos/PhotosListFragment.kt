package com.psd.learn.mysplash.ui.feed.photos

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.ViewModelFactory
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.PhotosFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.viewmodels.FeedPhotosViewModel

class PhotosListFragment :
    BaseListFragment<PhotoItem, PhotosFragmentLayoutBinding>(inflate = PhotosFragmentLayoutBinding::inflate) {

    private val viewModel by viewModels<FeedPhotosViewModel> { ViewModelFactory }

    private val feedPhotosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FeedPhotosAdapter(
            requestManager = Glide.with(this@PhotosListFragment),
            onItemClickListener = { photoId -> showMessageToast("clicked on photo have id: $photoId") },
            onProfileClickListener = { userId -> showMessageToast("clicked on user profile have id: $userId") }
        )
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun submitList(items: List<PhotoItem>) {
        feedPhotosAdapter.submitList(items)
    }

    override fun setupView() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = feedPhotosAdapter
        }
    }

    override fun setupViewModel() {
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) {
            renderUiState(it, binding.progressBar)
        }
        handleLoadMorePage(binding.recyclerView, viewModel)
    }

    companion object {
        fun newInstance() = PhotosListFragment()
    }
}