package com.psd.learn.mysplash.ui.feed.photos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.PhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.feed.FavoriteAction
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import com.psd.learn.mysplash.utils.log.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosListFragment :
    BasePagingFragment<PhotoItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate) {

    private val viewModel by activityViewModels<PagingFeedViewModel>()

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@PhotosListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override val recyclerView: RecyclerView
        get() = binding.recyclerView

    override val emptyTv: TextView
        get() = binding.loadingContainer.emptyList

    override val progressBar: ProgressBar
        get() = binding.loadingContainer.progressBar

    override val retryBtn: Button
        get() = binding.loadingContainer.retryButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagingData(viewModel.photoPagingDataFlow)
    }

    override fun handleCoverPhotoClicked(coverId: String?) {
        openPhotoDetails(coverId)
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        Logger.d("sangpd", "handleAddOrRemoveFavorite() - isFavorite: ${photoItem.isFavorite}")
        val currentState = photoItem.isFavorite
        viewModel.addOrRemoveFavorite(photoItem, currentState)
    }

    companion object {
        fun newInstance() = PhotosListFragment()
    }
}