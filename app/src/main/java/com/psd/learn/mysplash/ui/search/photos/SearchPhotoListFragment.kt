package com.psd.learn.mysplash.ui.search.photos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.SEARCH_PHOTOS_TYPE
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.SearchPhotoCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.PhotoPagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.BasePagingFragment
import com.psd.learn.mysplash.ui.feed.photos.favorite.AddOrRemoveFavoriteResult
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoriteAction
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotoHelper
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPhotoListFragment :
    BasePagingFragment<PhotoItem, SearchPhotoCollectionFragmentLayoutBinding>(inflate = SearchPhotoCollectionFragmentLayoutBinding::inflate), AddOrRemoveFavoriteResult {

    private val searchViewModel by activityViewModels<PagingSearchViewModel>()

    override val recyclerView: RecyclerView
        get() = binding.photoCollectionLayout.recyclerView

    override val emptyTv: TextView
        get() = binding.photoCollectionLayout.loadingContainer.emptyList

    override val progressBar: ProgressBar
        get() = binding.photoCollectionLayout.loadingContainer.progressBar

    override val retryBtn: Button
        get() = binding.photoCollectionLayout.loadingContainer.retryButton


    override val pagingAdapter: BasePagingAdapter<PhotoItem, out ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        PhotoPagingAdapter(
            requestManager = Glide.with(this@SearchPhotoListFragment),
            itemClickListener = mItemClickListener
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FavoritePhotoHelper.addResultListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleScroll(searchViewModel)
        bindPagingListWithLiveData(searchViewModel.searchPhotoPagingData)
        binSearchResult(searchViewModel.searchPhotoTotal, SEARCH_PHOTOS_TYPE, binding.searchResult)
    }

    override fun handleCoverPhotoClicked(item: PhotoItem) {
        openPhotoDetails(item)
    }

    override fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {
        val currentState = photoItem.isFavorite
        searchViewModel.addOrRemoveFavoriteFromSearch(currentState, photoItem)
    }

    companion object {
        fun newInstance() = SearchPhotoListFragment()
    }

    override fun updateFavorite(currentState: Boolean, photoItem: PhotoItem) {
        if (currentState) {
            searchViewModel.onFavoriteAction(FavoriteAction.AddFavorite(photoItem))
        } else {
            searchViewModel.onFavoriteAction(FavoriteAction.RemoveFavorite(photoItem))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FavoritePhotoHelper.removeResultListener(this)
    }
}