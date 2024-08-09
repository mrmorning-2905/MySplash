package com.psd.learn.mysplash.ui.feed.photos

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.MainActivity
import com.psd.learn.mysplash.MainViewModel
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.SORT_BY_TYPE_KEY
import com.psd.learn.mysplash.SortByType
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.PhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.bottomlayout.BottomMenuClickListener
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.core.UserArgs
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import com.psd.learn.mysplash.ui.list.SelectionModeManager
import com.psd.learn.mysplash.ui.utils.PreferenceUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PhotosListFragment :
    BasePagingFragment<PhotoItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate),
    BottomMenuClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private val pagingViewModel by activityViewModels<PagingFeedViewModel>()

    private val mainViewModel by activityViewModels<MainViewModel>()

    private val selectionManager by viewModels<SelectionModeManager>()

    private val bottomMenu by lazy {
        (requireActivity() as MainActivity).getBottomMenuLayout()
    }

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
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

    override val retryBtn: Button
        get() = binding.loadingContainer.retryButton

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.swipeRefresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentSortType = PreferenceUtils.getSortByType(requireContext(), SORT_BY_TYPE_KEY) ?: SortByType.LATEST_TYPE
        Log.d("sangpd", "onCreate_currentSortType: $currentSortType")
        pagingViewModel.updateSortByType(currentSortType)
        PreferenceUtils.registerPreferenceChangedListener(requireContext(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindPagingListWithLiveData(pagingViewModel.photoPagingFlow)
        observerSelectionMode()
        bottomMenu.addBottomMenuListener(this)
        pagingAdapter.addOnPagesUpdatedListener {
            val snapShotList = pagingAdapter.snapshot().items
            Log.d("sangpd", "onViewCreated() - current items paging list: ${snapShotList.size}")
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    selectionManager.updateListPhoto(snapShotList)
                }
                updateSelectAllCheckBox()
            }
        }
        observerSelectAllExecute()
    }

    override fun handleCoverPhotoClicked(item: PhotoItem) {
        if (selectionManager.isSelectionMode()) {
            selectionManager.addCheckedPhotoItem(item)
        } else {
            openPhotoDetails(item)
        }
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        pagingViewModel.addOrRemovePhotoItemToFavorite(requireContext(), photoItem)
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
        selectionManager.isChoiceMode.observe(viewLifecycleOwner) { isSelectionMode ->
            if (isSelectionMode && selectionManager.getListItemChecked().isNotEmpty()) {
                mainViewModel.showBottomMenu(true)
            }
            updateSelectAllCheckBox()
            pagingAdapter.notifyDataSetChanged()
        }

        selectionManager.listPhotoItemChecked.observe(viewLifecycleOwner) { checkedList ->
            if (bottomMenu.isBottomLayoutMenuShow() && checkedList.isEmpty()) {
                mainViewModel.showBottomMenu(false)
            } else if (!bottomMenu.isBottomLayoutMenuShow() && checkedList.isNotEmpty()) {
                mainViewModel.showBottomMenu(true)
            }
            updateSelectAllCheckBox()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observerSelectAllExecute() {
        binding.selectAllCheckbox.setOnClickListener {
            val isAllPhotoChecked = selectionManager.isAllPhotoChecked()
            selectionManager.checkAllPhotoItem(!isAllPhotoChecked)
            pagingAdapter.notifyDataSetChanged()
            updateSelectAllCheckBox()
        }
    }

    override fun onDestroyView() {
        pagingAdapter.removeOnPagesUpdatedListener {
            selectionManager.updateListPhoto(emptyList())
        }
        PreferenceUtils.removePreferenceChangedListener(requireContext(), this)
        bottomMenu.removeBottomMenuListener()
        super.onDestroyView()
    }

    override fun onBottomMenuClicked(menuId: Int) {
        val checkedList = selectionManager.getListItemChecked()
        Log.d("sangpd", "onBottomMenuClicked: ${checkedList.size}")
        when (menuId) {
            R.id.menu_cancel -> selectionManager.disableSelectionMode()
            R.id.menu_add_favorite -> {
                pagingViewModel.addMultiPhotoItemsToFavorite(requireContext(), checkedList) {
                    selectionManager.disableSelectionMode()
                }
            }
            R.id.menu_download -> {
                pagingViewModel.downloadCheckedFiles(requireContext(), checkedList, lifecycle) {}
                selectionManager.disableSelectionMode()
            }
        }
    }

    private fun updateSelectAllCheckBox() {
        val title: String
        val checkBoxDrawable: Int
        if (selectionManager.isSelectionMode()) {
            binding.selectAllContainer.visibility = View.VISIBLE
            val checkedList = selectionManager.getListItemChecked()
            val numberChecked = checkedList.size
            if (selectionManager.isAllPhotoChecked()) {
                val isEmptyCheckable = selectionManager.isEmptyCheckablePhoto()
                title = if (isEmptyCheckable) "Select All" else "$numberChecked items selected"
                checkBoxDrawable =
                    if (isEmptyCheckable) R.drawable.radio_unchecked else R.drawable.radio_checked
            } else if (checkedList.isNotEmpty()) {
                title = "$numberChecked items selected"
                checkBoxDrawable = R.drawable.radio_unchecked
            } else {
                title = "Select All"
                checkBoxDrawable = R.drawable.radio_unchecked
            }
            binding.selectAllTitle.text = title
            binding.selectAllCheckbox.setImageResource(checkBoxDrawable)
        } else {
            binding.selectAllContainer.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance() = PhotosListFragment()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == SORT_BY_TYPE_KEY) {
            val sortType = PreferenceUtils.getSortByType(requireContext(), key)
            Log.d("sangpd", "onSharedPreferenceChanged_sortType: $sortType")
            pagingViewModel.updateSortByType(sortType ?: SortByType.LATEST_TYPE)
        }
    }
}