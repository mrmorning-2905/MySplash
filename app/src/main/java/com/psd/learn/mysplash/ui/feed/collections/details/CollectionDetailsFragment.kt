package com.psd.learn.mysplash.ui.feed.collections.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.CollectionDetailsFragmentBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel

class CollectionDetailsFragment  :
    BasePagingFragment<PhotoItem, CollectionDetailsFragmentBinding>(inflate = CollectionDetailsFragmentBinding::inflate) {

    private val viewModel by activityViewModels<PagingFeedViewModel>()

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@CollectionDetailsFragment),
            itemClickListener = mItemClickListener
        )
    }

    override val recyclerView: RecyclerView
        get() = binding.photoCollectionList.recyclerView

    override val emptyTv: TextView
        get() = binding.photoCollectionList.loadingContainer.emptyList

    override val progressBar: ProgressBar
        get() = binding.photoCollectionList.loadingContainer.progressBar

    override val retryBtn: Button
        get() = binding.photoCollectionList.loadingContainer.retryButton

    private val collectionId: String
        get() = arguments?.getString("COLLECTION_ID") ?: ""

    private val collectionName: String
        get() = arguments?.getString("COLLECTION_NAME") ?: ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar(true, collectionName, true)
        return rootView
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
}