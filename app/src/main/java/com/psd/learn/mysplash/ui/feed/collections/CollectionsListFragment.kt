package com.psd.learn.mysplash.ui.feed.collections

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.FeedCollectionsFragmentLayoutBinding
import com.psd.learn.mysplash.ui.CollectionPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsListFragment: BasePagingFragment<CollectionItem, FeedCollectionsFragmentLayoutBinding>(inflate = FeedCollectionsFragmentLayoutBinding::inflate) {

    override val pagingAdapter: BasePagingAdapter<CollectionItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        CollectionPagingAdapter(
            requestManager = Glide.with(this@CollectionsListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override val recyclerView: RecyclerView
        get() = binding.recyclerView

    private val viewModel by activityViewModels<PagingFeedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagingData(viewModel.collectionPagingDataFlow)
    }

    companion object {
        fun newInstance() = CollectionsListFragment()
    }
}