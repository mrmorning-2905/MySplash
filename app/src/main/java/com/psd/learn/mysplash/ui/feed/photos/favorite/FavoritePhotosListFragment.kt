package com.psd.learn.mysplash.ui.feed.photos.favorite

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.PhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.core.UserArgs
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel

class FavoritePhotosListFragment :
    BasePagingFragment<PhotoItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate){

    private val pagingViewModel by activityViewModels<PagingFeedViewModel>()

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@FavoritePhotosListFragment),
            itemClickListener = mItemClickListener,
            needShowProfile = true
        )
    }

    override val recyclerView: RecyclerView
    get() = binding.recyclerView

    override val emptyTv: TextView
    get() = binding.loadingContainer.emptyList

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.swipeRefresh

    override val retryBtn: Button
    get() = binding.loadingContainer.retryButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagingData(pagingViewModel.favoritePhotoFlow)
    }

    override fun handleCoverPhotoClicked(item: PhotoItem) {
        openPhotoDetails(item)
    }

    override fun handleProfileClicked(userInfo: UserArgs) {
        openUserDetails(userInfo)
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        pagingViewModel.addOrRemovePhotoItemToFavorite(requireContext(), photoItem)
    }

    companion object {
        fun newInstance() = FavoritePhotosListFragment()
    }

    private fun setupOptionMenu() {

    }
}