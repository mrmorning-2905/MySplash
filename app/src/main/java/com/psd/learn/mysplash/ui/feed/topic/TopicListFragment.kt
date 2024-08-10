package com.psd.learn.mysplash.ui.feed.topic

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.PHOTO_SORT_BY_TYPE_KEY
import com.psd.learn.mysplash.SortByType
import com.psd.learn.mysplash.TOPIC_SORT_BY_TYPE_KEY
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.PhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.CollectionPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.core.UserArgs
import com.psd.learn.mysplash.ui.feed.PagingFeedViewModel
import com.psd.learn.mysplash.ui.feed.SortByState
import com.psd.learn.mysplash.ui.utils.PreferenceUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicListFragment :
    BasePagingFragment<CollectionItem, PhotoCollectionFragmentLayoutBinding>(inflate = PhotoCollectionFragmentLayoutBinding::inflate),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override val pagingAdapter: BasePagingAdapter<CollectionItem, out ViewBinding> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentSortType = PreferenceUtils.getSortByType(requireContext(), TOPIC_SORT_BY_TYPE_KEY) ?: SortByType.POSITION_TYPE
        Log.d("sangpd", "TopicListFragment_onCreate_currentSortType: $currentSortType")
        pagingViewModel.updateSortByType(SortByState.TopicSortByState(currentSortType))
        PreferenceUtils.registerPreferenceChangedListener(requireContext(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagingData(pagingViewModel.topicPagingDataFlow)
    }

    override fun handleCoverPhotoClicked(item: CollectionItem) {
        openCollectionDetails(item)
    }

    override fun handleProfileClicked(userInfo: UserArgs) {
        openUserDetails(userInfo)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == TOPIC_SORT_BY_TYPE_KEY) {
            val sortType = PreferenceUtils.getSortByType(requireContext(), key)
            Log.d("sangpd", "onSharedPreferenceChanged_sortType: $sortType")
            pagingViewModel.updateSortByType(SortByState.TopicSortByState(sortType ?: SortByType.POSITION_TYPE))
        }
    }

    override fun onDestroyView() {
        PreferenceUtils.removePreferenceChangedListener(requireContext(), this)
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = TopicListFragment()
    }
}