package com.psd.learn.mysplash.ui.feed.photos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
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
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import com.psd.learn.mysplash.ui.list.SelectionModeManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosListFragment :
    BasePagingFragment<PhotoItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate){

    private val viewModel by activityViewModels<PagingFeedViewModel>()

    private val selectionManager by viewModels<SelectionModeManager>()

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@PhotosListFragment),
            itemClickListener = mItemClickListener,
            needShowProfile = true,
            selectionManager = selectionManager
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
        bindPagingListWithLiveData(viewModel.photoPagingFlow)
        observerSelectionMode()
    }

    override fun handleCoverPhotoClicked(item: PhotoItem) {
        if (selectionManager.isSelectionMode()) {
            selectionManager.addCheckedPhotoItem(item)
        } else {
            openPhotoDetails(item)
        }
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        executeFavorite(photoItem)
    }

    override fun handleProfileClicked(userInfo: UserArgs) {
        openUserDetails(userInfo)
    }

    override fun handleCoverPhotoLongClicked(photoItem: PhotoItem) {
        if (!selectionManager.isSelectionMode()) {
            selectionManager.enableSelectionMode()
        }
        selectionManager.addCheckedPhotoItem(photoItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observerSelectionMode() {
        selectionManager.isChoiceMode.observe(viewLifecycleOwner) {
            //todo show bottom menu to download checked files or mark as favorite
            pagingAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance() = PhotosListFragment()
    }
}