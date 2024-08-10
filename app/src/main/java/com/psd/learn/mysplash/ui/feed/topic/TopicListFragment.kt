package com.psd.learn.mysplash.ui.feed.topic

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.PhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.CollectionPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.core.UserArgs
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicListFragment: BasePagingFragment<CollectionItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate) {

    override val pagingAdapter: BasePagingAdapter<CollectionItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        CollectionPagingAdapter(
            requestManager = Glide.with(this@TopicListFragment),
            itemClickListener = mItemClickListener,
            needShowProfile = false
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

    private val pagingViewModel by activityViewModels<PagingFeedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagingData(pagingViewModel.collectionPagingDataFlow)
    }

    override fun handleCoverPhotoClicked(item: CollectionItem) {
        openCollectionDetails(item)
    }

    override fun handleProfileClicked(userInfo: UserArgs) {
        openUserDetails(userInfo)
    }

    companion object {
        fun newInstance() = TopicListFragment()
    }
}