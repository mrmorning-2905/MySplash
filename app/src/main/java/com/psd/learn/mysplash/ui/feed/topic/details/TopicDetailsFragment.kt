package com.psd.learn.mysplash.ui.feed.topic.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.AUTO_SET_WALLPAPER_KEY
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.SortByType
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.CollectionDetailsFragmentBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.core.UserArgs
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import com.psd.learn.mysplash.ui.utils.PreferenceUtils
import com.psd.learn.mysplash.worker.AutoSetWallpaperWorker
import com.psd.learn.mysplash.worker.AutoSetWallpaperWorker.Companion.AUTO_REQUEST_INTERVAL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicDetailsFragment :
    BasePagingFragment<PhotoItem, CollectionDetailsFragmentBinding>(inflate = CollectionDetailsFragmentBinding::inflate) {

    private val topicDetailsViewModel by viewModels<TopicDetailsViewModel>()

    private val pagingViewModel by activityViewModels<PagingFeedViewModel>()

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@TopicDetailsFragment),
            itemClickListener = mItemClickListener,
            needShowProfile = true
        )
    }

    override val recyclerView: RecyclerView
        get() = binding.photoCollectionList.recyclerView

    override val emptyTv: TextView
        get() = binding.photoCollectionList.loadingContainer.emptyList

    override val retryBtn: Button
        get() = binding.photoCollectionList.loadingContainer.retryButton

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.photoCollectionList.swipeRefresh

    private val topicArgs by lazy(LazyThreadSafetyMode.NONE) { navArgs<TopicDetailsFragmentArgs>().value.collectionInfo }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar(true, topicArgs.coverDescription, true)
        setupMenuProvider(topicId = topicArgs.collectionId)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindHeader()
        bindPagingListWithLiveData(topicDetailsViewModel.topicPhotos)
    }

    @SuppressLint("SetTextI18n")
    private fun bindHeader() {
        binding.run {
            totalImage.text = "Total: ${topicArgs.numberImages} Images"
            description.text = topicArgs.collectionDescription
        }
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
}