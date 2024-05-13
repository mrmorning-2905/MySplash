package com.psd.learn.mysplash.ui.feed.photos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.ServiceLocator
import com.psd.learn.mysplash.ViewModelFactory
import com.psd.learn.mysplash.databinding.PhotosFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.core.UiState

class PhotosFragment : BaseFragment<PhotosFragmentLayoutBinding>(inflate = PhotosFragmentLayoutBinding::inflate) {

    private val viewModel by viewModels<FeedPhotosViewModel> { ViewModelFactory }

    private val feedPhotosAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FeedPhotosAdapter(requestManager = Glide.with(this@PhotosFragment))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = feedPhotosAdapter
        }
    }

    private fun setupViewModel() {
        Log.d("sangpd", "setupViewModel_viewModel: $viewModel")
        viewModel.uiStateLiveData.observe(viewLifecycleOwner, ::renderState)
        handleLoadMorePage()
    }

    private fun renderState(state: UiState<PhotoItem>) {
        when (state) {
            is UiState.Content -> {
                binding.progressBar.visibility = if (state.nextPageState is UiState.NextPageState.Loading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                feedPhotosAdapter.submitList(state.items)
            }

            is UiState.FirstPageError -> {
                binding.progressBar.visibility = View.GONE
                feedPhotosAdapter.submitList(emptyList())
            }

            is UiState.FirstPageLoading -> {
                binding.progressBar.visibility = View.VISIBLE
                feedPhotosAdapter.submitList(emptyList())
            }
        }

    }

    private fun handleLoadMorePage() {
        val linearLayoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0
                    && linearLayoutManager.findLastVisibleItemPosition() + VISIBLE_THRESHOLD >= linearLayoutManager.itemCount) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    companion object {
        const val VISIBLE_THRESHOLD = 3
        fun newInstance() = PhotosFragment()
    }
}