package com.psd.learn.mysplash.ui.feed.collections.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.CollectionDetailsFragmentBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.core.UserArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionDetailsFragment  :
    BasePagingFragment<PhotoItem, CollectionDetailsFragmentBinding>(inflate = CollectionDetailsFragmentBinding::inflate) {

    private val collectionDetailsViewModel by viewModels<CollectionDetailsViewModel>()

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@CollectionDetailsFragment),
            itemClickListener = mItemClickListener,
            needShowProfile = true
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

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.photoCollectionList.swipeRefresh

    private val collectionInfoArgs by lazy(LazyThreadSafetyMode.NONE) { navArgs<CollectionDetailsFragmentArgs>().value.collectionInfo }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar(true, collectionInfoArgs.coverDescription, true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindHeader()
        bindPagingListWithLiveData(collectionDetailsViewModel.collectionPhotos)
    }

    @SuppressLint("SetTextI18n")
    private fun bindHeader() {
        binding.run {
            totalImage.text = "Total: ${collectionInfoArgs.numberImages} Images"
            userCreate.text = "Created by: ${collectionInfoArgs.userNameAccount}"
        }
    }
    override fun handleCoverPhotoClicked(item: PhotoItem) {
        openPhotoDetails(item)
    }

    override fun handleProfileClicked(userInfo: UserArgs) {
        openUserDetails(userInfo)
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        executeFavorite(photoItem)
    }
}