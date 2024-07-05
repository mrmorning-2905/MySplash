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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.CollectionDetailsFragmentBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionDetailsFragment  :
    BasePagingFragment<PhotoItem, CollectionDetailsFragmentBinding>(inflate = CollectionDetailsFragmentBinding::inflate) {

    private val collectionDetailsViewModel by activityViewModels<CollectionDetailsViewModel>()

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

    private val collectionInfo: CollectionItem?
        get() = arguments?.getParcelable("COLLECTION_INFO")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val collectionId = collectionInfo?.collectionId
        collectionId?.let {
            collectionDetailsViewModel.setCollectionId(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar(true, collectionInfo?.coverDescription ?: "", true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val collectionDescription = collectionInfo?.let { "${it.numberImages} Images - Created by: ${it.userName}" } ?: ""
        binding.collectionDescriptionHeader.text = collectionDescription
        bindPagingListWithLiveData(collectionDetailsViewModel.collectionPhotos)
    }

    override fun handleCoverPhotoClicked(item: PhotoItem) {
        openPhotoDetails(item)
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        val currentState = photoItem.isFavorite
        collectionDetailsViewModel.addOrRemoveFavoriteFromCollectionDetails(currentState, photoItem)
    }
}