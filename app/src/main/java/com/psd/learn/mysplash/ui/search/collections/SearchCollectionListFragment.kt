package com.psd.learn.mysplash.ui.search.collections

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.SEARCH_COLLECTIONS_TYPE
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.SearchPhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.CollectionPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCollectionListFragment :
    BasePagingFragment<CollectionItem, SearchPhotoCollectionFragmentLayoutBinding>(inflate = SearchPhotoCollectionFragmentLayoutBinding::inflate) {

    private val searchViewModel by activityViewModels<PagingSearchViewModel>()

    override val recyclerView: RecyclerView
        get() = binding.photoCollectionLayout.recyclerView

    override val emptyTv: TextView
        get() = binding.photoCollectionLayout.loadingContainer.emptyList

    override val progressBar: ProgressBar
        get() = binding.photoCollectionLayout.loadingContainer.progressBar

    override val retryBtn: Button
        get() = binding.photoCollectionLayout.loadingContainer.retryButton

    override val pagingAdapter: BasePagingAdapter<CollectionItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        CollectionPagingAdapter(
            requestManager = Glide.with(this@SearchCollectionListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleScroll(searchViewModel)
        initPagingData(searchViewModel.searchCollectionPagingData)
        binSearchResult(searchViewModel.searchCollectionTotal, SEARCH_COLLECTIONS_TYPE, binding.searchResult)
    }

    override fun handleCoverPhotoClicked(item: CollectionItem) {
        openCollectionDetails(item)
    }

    companion object {
        fun newInstance() = SearchCollectionListFragment()
    }
}