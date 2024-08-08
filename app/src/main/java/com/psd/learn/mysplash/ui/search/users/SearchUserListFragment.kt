package com.psd.learn.mysplash.ui.search.users

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.SEARCH_USERS_TYPE
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.SearchUserFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.core.UserArgs
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchUserListFragment :
    BasePagingFragment<UserItem, SearchUserFragmentLayoutBinding>(inflate = SearchUserFragmentLayoutBinding::inflate) {

    private val searchViewModel by activityViewModels<PagingSearchViewModel>()

    override val recyclerView: RecyclerView
        get() = binding.recyclerView

    override val emptyTv: TextView
        get() = binding.loadingContainer.emptyList

    override val retryBtn: Button
        get() = binding.loadingContainer.retryButton

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.swipeRefresh

    override val pagingAdapter: BasePagingAdapter<UserItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE){
        SearchUserListAdapter(
            requestManager = Glide.with(this@SearchUserListFragment),
            itemClickListener = mItemClickListener,
            childItemClickListener = {photoItem -> openPhotoDetails(photoItem)}
        )
    }

    override fun handleProfileClicked(userInfo: UserArgs) {
        openUserDetails(userInfo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleScroll(searchViewModel)
        initPagingData(searchViewModel.searchUserPagingData)
        binSearchResult(searchViewModel.resultStateFlow, SEARCH_USERS_TYPE, binding.searchResult)
    }

    companion object {
        fun newInstance() = SearchUserListFragment()
    }
}