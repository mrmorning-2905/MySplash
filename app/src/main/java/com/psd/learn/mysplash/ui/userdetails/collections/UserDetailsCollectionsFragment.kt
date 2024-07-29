package com.psd.learn.mysplash.ui.userdetails.collections

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.PhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.CollectionPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.userdetails.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsCollectionsFragment :
    BasePagingFragment<CollectionItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate) {

    private val viewModel by viewModels<UserDetailsViewModel>({requireParentFragment()})

    override val pagingAdapter: BasePagingAdapter<CollectionItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        CollectionPagingAdapter(
            requestManager = Glide.with(this@UserDetailsCollectionsFragment),
            itemClickListener = mItemClickListener,
            needShowProfile = false
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

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.swipeRefresh

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagingData(viewModel.userDetailsCollectionsPagingData)
    }

    override fun handleCoverPhotoClicked(item: CollectionItem) {
        openCollectionDetails(item)
    }

    companion object {
        fun newInstance() = UserDetailsCollectionsFragment()
    }
}