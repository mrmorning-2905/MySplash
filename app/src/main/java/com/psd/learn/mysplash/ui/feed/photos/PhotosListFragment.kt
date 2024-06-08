package com.psd.learn.mysplash.ui.feed.photos

import android.os.Bundle
import android.view.View
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
import dagger.hilt.android.AndroidEntryPoint

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagingData(viewModel.photoPagingDataFlow)
    }

    companion object {
        fun newInstance() = PhotosListFragment()
    }
}