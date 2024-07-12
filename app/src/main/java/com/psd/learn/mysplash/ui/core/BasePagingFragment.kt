package com.psd.learn.mysplash.ui.core

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.SEARCH_COLLECTIONS_TYPE
import com.psd.learn.mysplash.SEARCH_PHOTOS_TYPE
import com.psd.learn.mysplash.SEARCH_USERS_TYPE
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.ui.feed.FeedFragmentDirections
import com.psd.learn.mysplash.ui.feed.collections.details.CollectionDetailsFragmentDirections
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import com.psd.learn.mysplash.ui.search.ResultSearchState
import com.psd.learn.mysplash.ui.search.SearchAction
import com.psd.learn.mysplash.ui.search.SearchFragmentDirections
import com.psd.learn.mysplash.ui.userdetails.UserDetailsFragmentDirections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BasePagingFragment<T : Any, VB : ViewBinding>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment<VB>(inflate) {
    override val TAG: String
        get() = BasePagingFragment::class.java.simpleName

    private var gridLayoutManager: GridLayoutManager? = null

    abstract val pagingAdapter: BasePagingAdapter<T, out ViewBinding>

    abstract val recyclerView: RecyclerView

    abstract val emptyTv: TextView

    abstract val progressBar: ProgressBar

    abstract val retryBtn: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    protected fun handleScroll(
        viewModel: PagingSearchViewModel,
    ) {
        val uiState = viewModel.uiState
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) {
                    val scrollAction = SearchAction.Scroll(currentQuery = uiState.value.query)
                    viewModel.onApplyUserAction(scrollAction)
                }
            }
        })

        val notLoading = pagingAdapter
            .loadStateFlow
            .distinctUntilChangedBy { it.source.refresh }
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrollForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrollForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        lifecycleScope.launch {
            shouldScrollToTop
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { shouldScroll ->
                    if (shouldScroll) {
                        recyclerView.scrollToPosition(0)
                    }
                }
        }
    }

    private fun initAdapter() {
        gridLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { pagingAdapter.retry() },
                footer = PagingLoadStateAdapter { pagingAdapter.retry() }
            )
        }

        handleLoadState()
    }

    private fun initLoadState() {
        emptyTv.isVisible = true
        progressBar.isVisible = false
        retryBtn.isVisible = false
    }

    private fun handleLoadState() {
        initLoadState()
        lifecycleScope.launch {
            pagingAdapter.loadStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { loadState ->
                    val isListEmpty = loadState.refresh is LoadState.NotLoading && pagingAdapter.itemCount == 0
                    emptyTv.isVisible = isListEmpty
                    recyclerView.isVisible = !isListEmpty
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    retryBtn.isVisible = loadState.source.refresh is LoadState.Error
                }
        }

        retryBtn.apply {
            if (isVisible) {
                setOnClickListener {
                    pagingAdapter.retry()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    protected fun binSearchResult(resultStateFlow: StateFlow<ResultSearchState>, searchType: Int, resultTv: TextView) {
        lifecycleScope.launch {
            resultStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collectLatest { resultState ->
                    Log.d("sangpd", "binSearchResult_searchType: $searchType - resultState: $resultState")
                    val searchTypeDes = getSearchTypeString(searchType)
                    resultTv.text = "Result: ${resultState.resultMap[searchType]} $searchTypeDes"
                }
        }
    }

    private fun getSearchTypeString(searchType: Int): String {
        return when (searchType) {
            SEARCH_PHOTOS_TYPE -> "Images"
            SEARCH_COLLECTIONS_TYPE -> "Collections"
            SEARCH_USERS_TYPE -> "Users"
            else -> "UnKnown"
        }
    }

    protected fun initPagingData(
        pagingData: Flow<PagingData<T>>
    ) {
        lifecycleScope.launch {
            pagingData
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest(pagingAdapter::submitData)
        }
    }

    protected fun bindPagingListWithLiveData(
        pagingData: LiveData<PagingData<T>>
    ) {
        pagingData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                pagingAdapter.submitData(it)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        gridLayoutManager?.let {
            it.spanCount = resources.getInteger(R.integer.grid_column_count)
        }
    }

    protected open val mItemClickListener = object : OnItemClickListener<T> {
        override fun coverPhotoClicked(item: T) {
            handleCoverPhotoClicked(item)
        }

        override fun profileClicked(userInfo: UserArgs) {
            handleProfileClicked(userInfo)
        }

        override fun addOrRemoveFavorite(photoItem: PhotoItem) {
            handleAddOrRemoveFavorite(photoItem)
        }
    }

    open fun handleProfileClicked(userInfo: UserArgs) {}
    open fun handleCoverPhotoClicked(item: T) {}
    open fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {}

    protected fun openPhotoDetails(photoItem: PhotoItem) {
        val navHost = findNavController()
        val action = when (val currentDestId = navHost.currentDestination?.id) {
            R.id.feed_fragment_dest -> FeedFragmentDirections.actionFeedFragmentToDetailsPhotoFragment(photoId = photoItem.photoId)
            R.id.search_fragment_dest -> SearchFragmentDirections.actionSearchFragmentToDetailsPhotoFragment(photoId = photoItem.photoId)
            R.id.collection_details_fragment_dest -> CollectionDetailsFragmentDirections.actionCollectionDetailsToDetailsPhotoFragment(photoId = photoItem.photoId)
            R.id.user_details_fragment_dest -> UserDetailsFragmentDirections.actionUserDetailsToPhotoDetails(photoId = photoItem.photoId)
            else -> error("openPhotoDetails() - doesn't support action at this fragment_currentDestId: $currentDestId")
        }
        navHost.navigate(action)
    }

    protected fun openUserDetails(userInfo: UserArgs) {
        val navHost = findNavController()
        val action = when (val currentDestId = navHost.currentDestination?.id) {
            R.id.feed_fragment_dest -> FeedFragmentDirections.actionFeedFragmentToUserDetailsFragment(userInfoArgs = userInfo)
            R.id.search_fragment_dest -> SearchFragmentDirections.actionSearchFragmentToUserDetailsFragment(userInfoArgs = userInfo)
            R.id.collection_details_fragment_dest -> CollectionDetailsFragmentDirections.actionCollectionDetailsFragmentToUserDetailsFragment(userInfoArgs = userInfo)
            R.id.user_details_fragment_dest -> UserDetailsFragmentDirections.actionUserDetailsToUserDetails(userInfoArgs = userInfo)
            else -> error("openUserDetails() - doesn't support action at this fragment_currentDestId: $currentDestId")
        }
        navHost.navigate(action)
    }

    protected fun openCollectionDetails(collectionItem: CollectionItem) {
        val navHost = findNavController()
        val action = when (val currentDestId = navHost.currentDestination?.id) {
            R.id.feed_fragment_dest -> FeedFragmentDirections.actionFeedFragmentToCollectionDetailsFragment(collectionItem)
            R.id.search_fragment_dest -> SearchFragmentDirections.actionSearchFragmentToCollectionDetailsFragment(collectionItem)
            R.id.user_details_fragment_dest -> UserDetailsFragmentDirections.actionUserDetailsToCollectionDetails(collectionItem)
            else -> error("openCollectionDetails() - doesn't support action at this fragment_currentDestId: $currentDestId")
        }
        navHost.navigate(action)

    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }
}