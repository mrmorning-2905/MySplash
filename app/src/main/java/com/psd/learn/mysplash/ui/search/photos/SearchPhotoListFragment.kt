package com.psd.learn.mysplash.ui.search.photos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.SearchPhotoFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPhotoListFragment :
    BasePagingFragment<PhotoItem, SearchPhotoFragmentLayoutBinding>(inflate = SearchPhotoFragmentLayoutBinding::inflate) {

    private val searchViewModel by activityViewModels<PagingSearchViewModel>()

    override val recyclerView: RecyclerView
        get() = binding.recyclerView

    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        SearchPhotoPagingAdapter(
            requestManager = Glide.with(this@SearchPhotoListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleScroll(searchViewModel, searchViewModel.uiState)
        initPagingData(searchViewModel.searchPhotoPagingData)
    }

    companion object {
        fun newInstance() = SearchPhotoListFragment()
    }
}