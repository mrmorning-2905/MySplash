package com.psd.learn.mysplash.ui.userdetails.photos

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
import com.psd.learn.mysplash.ui.userdetails.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailPhotosFragment : BasePagingFragment<PhotoItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate) {

    private val userDetailsViewModel by viewModels<UserDetailsViewModel>({requireParentFragment()})

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@UserDetailPhotosFragment),
            itemClickListener = mItemClickListener,
            needShowProfile = false
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
        bindPagingListWithLiveData(userDetailsViewModel.userDetailsPhotosPagingData)
    }

    override fun handleCoverPhotoClicked(item: PhotoItem) {
        openPhotoDetails(item)
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        userDetailsViewModel.addOrRemoveFavoritePhotoItem(requireContext(), photoItem)
    }

    companion object {
        fun newInstance() = UserDetailPhotosFragment()
    }
}