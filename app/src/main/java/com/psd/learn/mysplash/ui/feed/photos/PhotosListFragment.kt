package com.psd.learn.mysplash.ui.feed.photos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.PhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import com.psd.learn.mysplash.ui.feed.photos.favorite.AddOrRemoveFavoriteResult
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoriteAction
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotoHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosListFragment :
    BasePagingFragment<PhotoItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate), AddOrRemoveFavoriteResult {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FavoritePhotoHelper.addResultListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindPagingListWithLiveData(viewModel.photoPagingDataFlow)
    }

    override fun handleCoverPhotoClicked(item: PhotoItem) {
        openPhotoDetails(item)
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        val currentState = photoItem.isFavorite
        viewModel.addOrRemoveFavoriteFromFeed(currentState, photoItem)
    }

    companion object {
        fun newInstance() = PhotosListFragment()
    }

    override fun updateFavorite(currentState: Boolean, photoItem: PhotoItem) {
        if (currentState) {
            viewModel.onFavoriteAction(FavoriteAction.AddFavorite(photoItem))
        } else {
            viewModel.onFavoriteAction(FavoriteAction.RemoveFavorite(photoItem))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FavoritePhotoHelper.removeResultListener(this)
    }
}