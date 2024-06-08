package com.psd.learn.mysplash.ui.search.users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.SearchUserFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchUserListFragment :
    BasePagingFragment<UserItem, SearchUserFragmentLayoutBinding>(inflate = SearchUserFragmentLayoutBinding::inflate) {

    private val searchViewModel by activityViewModels<PagingSearchViewModel>()

    override val recyclerView: RecyclerView
        get() = binding.recyclerView

    override val pagingAdapter: BasePagingAdapter<UserItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE){
        SearchUserListAdapter(
            requestManager = Glide.with(this@SearchUserListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleScroll(searchViewModel)
        initPagingData(searchViewModel.searchUserPagingData)
    }

    companion object {
        fun newInstance() = SearchUserListFragment()
    }
}