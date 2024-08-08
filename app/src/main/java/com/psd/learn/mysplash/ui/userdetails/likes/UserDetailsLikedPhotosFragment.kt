package com.psd.learn.mysplash.ui.userdetails.likes

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
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
import com.psd.learn.mysplash.ui.userdetails.UserDetailsViewModel

class UserDetailsLikedPhotosFragment :
    BasePagingFragment<PhotoItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate) {
    private val userDetailViewModel by viewModels<UserDetailsViewModel>({ requireParentFragment() })

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@UserDetailsLikedPhotosFragment),
            itemClickListener = mItemClickListener,
            needShowProfile = true
        )
    }
    override val recyclerView: RecyclerView
        get() = binding.recyclerView

    override val emptyTv: TextView
        get() = binding.loadingContainer.emptyList

    override val retryBtn: Button
        get() = binding.loadingContainer.retryButton

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.swipeRefresh

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindPagingListWithLiveData(userDetailViewModel.userDetailsLikedPagingData)
    }

    override fun handleCoverPhotoClicked(item: PhotoItem) {
        openPhotoDetails(item)
    }

    override fun handleProfileClicked(userInfo: UserArgs) {
        openUserDetails(userInfo)
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        userDetailViewModel.addOrRemoveFavoritePhotoItem(requireContext(), photoItem)
    }

    companion object {
        fun newInstance() = UserDetailsLikedPhotosFragment()
    }
}