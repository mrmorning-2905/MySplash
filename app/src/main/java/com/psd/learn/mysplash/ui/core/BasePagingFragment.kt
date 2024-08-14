package com.psd.learn.mysplash.ui.core

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.AUTO_SET_WALLPAPER_KEY
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.SEARCH_COLLECTIONS_TYPE
import com.psd.learn.mysplash.SEARCH_PHOTOS_TYPE
import com.psd.learn.mysplash.SEARCH_USERS_TYPE
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.ui.feed.FeedFragmentDirections
import com.psd.learn.mysplash.ui.feed.collections.details.CollectionDetailsFragmentDirections
import com.psd.learn.mysplash.ui.feed.photos.wallpaper.WallpaperHistoryListFragmentDirections
import com.psd.learn.mysplash.ui.feed.topic.details.TopicDetailsFragmentDirections
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import com.psd.learn.mysplash.ui.search.ResultSearchState
import com.psd.learn.mysplash.ui.search.SearchAction
import com.psd.learn.mysplash.ui.search.SearchFragmentDirections
import com.psd.learn.mysplash.ui.userdetails.UserDetailsFragmentDirections
import com.psd.learn.mysplash.ui.utils.PreferenceUtils
import com.psd.learn.mysplash.ui.utils.safeHandleClickListener
import com.psd.learn.mysplash.worker.AutoSetWallpaperWorker
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

    private var staggeredLayoutManager: StaggeredGridLayoutManager? = null

    abstract val pagingAdapter: BasePagingAdapter<T, out ViewBinding>

    abstract val recyclerView: RecyclerView

    abstract val emptyTv: TextView

    abstract val swipeRefreshLayout: SwipeRefreshLayout

    abstract val retryBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        staggeredLayoutManager =
            StaggeredGridLayoutManager(
                resources.getInteger(R.integer.grid_column_count),
                StaggeredGridLayoutManager.VERTICAL
            )
        staggeredLayoutManager?.gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSwipeToRefresh()
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

    private fun initSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            pagingAdapter.refresh()
        }
        lifecycleScope.launch {
            pagingAdapter
                .loadStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { loadState ->
                    swipeRefreshLayout.isRefreshing = loadState.refresh is LoadState.Loading
                }
        }
    }

    private fun initAdapter() {
        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = staggeredLayoutManager
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { pagingAdapter.retry() },
                footer = PagingLoadStateAdapter { pagingAdapter.retry() }
            )
        }
        handleLoadState()
    }

    private fun initLoadState() {
        emptyTv.isVisible = true
        retryBtn.isVisible = false
    }

    private fun handleLoadState() {
        initLoadState()

        retryBtn.safeHandleClickListener {
            pagingAdapter.retry()
        }

        lifecycleScope.launch {
            pagingAdapter.loadStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { loadState ->
                    val isListEmpty =
                        loadState.refresh is LoadState.NotLoading && pagingAdapter.itemCount == 0
                    emptyTv.isVisible = isListEmpty
                    recyclerView.isVisible = !isListEmpty
                    retryBtn.isVisible = loadState.source.refresh is LoadState.Error
                }
        }
    }

    @SuppressLint("SetTextI18n")
    protected fun binSearchResult(
        resultStateFlow: StateFlow<ResultSearchState>,
        searchType: Int,
        resultTv: TextView
    ) {
        resultTv.visibility = View.GONE
        lifecycleScope.launch {
            resultStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collectLatest { resultState ->
                    val searchTypeDes = getSearchTypeString(searchType)
                    val totalSearch = resultState.resultMap[searchType]
                    if (totalSearch != null) {
                        resultTv.visibility = View.VISIBLE
                        resultTv.text =
                            "Result: ${resultState.resultMap[searchType]} $searchTypeDes"
                    }
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
        staggeredLayoutManager?.spanCount = resources.getInteger(R.integer.grid_column_count)
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

        override fun coverPhotoLongClicked(photoItem: PhotoItem) {
            handleCoverPhotoLongClicked(photoItem)
        }
    }

    open fun handleProfileClicked(userInfo: UserArgs) {}
    open fun handleCoverPhotoClicked(item: T) {}
    open fun handleAddOrRemoveFavorite(photoItem: PhotoItem) {}
    open fun handleCoverPhotoLongClicked(photoItem: PhotoItem) {}

    protected fun openPhotoDetails(photoItem: PhotoItem) {
        val navHost = findNavController()
        val action = when (val currentDestId = navHost.currentDestination?.id) {
            R.id.feed_fragment_dest -> FeedFragmentDirections.actionFeedFragmentToDetailsPhotoFragment(
                photoId = photoItem.photoId
            )

            R.id.search_fragment_dest -> SearchFragmentDirections.actionSearchFragmentToDetailsPhotoFragment(
                photoId = photoItem.photoId
            )

            R.id.collection_details_fragment_dest -> CollectionDetailsFragmentDirections.actionCollectionDetailsToDetailsPhotoFragment(
                photoId = photoItem.photoId
            )

            R.id.user_details_fragment_dest -> UserDetailsFragmentDirections.actionUserDetailsToPhotoDetails(
                photoId = photoItem.photoId
            )

            R.id.topic_details_fragment_dest -> TopicDetailsFragmentDirections.actionTopicDetailsToDetailsPhotoFragment(
                photoId = photoItem.photoId
            )

            R.id.wallpaper_history_fragment_dest -> WallpaperHistoryListFragmentDirections.actionWallpaperHistoryToDetailsPhotoFragment(
                photoId = photoItem.photoId
            )

            else -> error("openPhotoDetails() - doesn't support action at this fragment_currentDestId: $currentDestId")
        }
        navHost.navigate(action)
    }

    protected fun openUserDetails(userInfo: UserArgs) {
        val navHost = findNavController()
        val action = when (val currentDestId = navHost.currentDestination?.id) {
            R.id.feed_fragment_dest -> FeedFragmentDirections.actionFeedFragmentToUserDetailsFragment(
                userInfoArgs = userInfo
            )

            R.id.search_fragment_dest -> SearchFragmentDirections.actionSearchFragmentToUserDetailsFragment(
                userInfoArgs = userInfo
            )

            R.id.collection_details_fragment_dest -> CollectionDetailsFragmentDirections.actionCollectionDetailsFragmentToUserDetailsFragment(
                userInfoArgs = userInfo
            )

            R.id.user_details_fragment_dest -> UserDetailsFragmentDirections.actionUserDetailsToUserDetails(
                userInfoArgs = userInfo
            )

            R.id.topic_details_fragment_dest -> TopicDetailsFragmentDirections.actionTopicDetailsFragmentToUserDetailsFragment(
                userInfoArgs = userInfo
            )

            R.id.wallpaper_history_fragment_dest -> WallpaperHistoryListFragmentDirections.actionWallpaperHistoryToUserDetailsFragment(
                userInfoArgs = userInfo
            )

            else -> error("openUserDetails() - doesn't support action at this fragment_currentDestId: $currentDestId")
        }
        navHost.navigate(action)
    }

    protected fun openCollectionDetails(collectionItem: CollectionItem, isTopicFragment: Boolean = false) {
        val navHost = findNavController()
        val action = when (val currentDestId = navHost.currentDestination?.id) {
            R.id.feed_fragment_dest -> {
                if (isTopicFragment) {
                    FeedFragmentDirections.actionFeedFragmentToTopicDetailsFragment(collectionItem)
                } else {
                    FeedFragmentDirections.actionFeedFragmentToCollectionDetailsFragment(collectionItem)
                }
            }

            R.id.search_fragment_dest -> SearchFragmentDirections.actionSearchFragmentToCollectionDetailsFragment(
                collectionItem
            )

            R.id.user_details_fragment_dest -> UserDetailsFragmentDirections.actionUserDetailsToCollectionDetails(
                collectionItem
            )

            else -> error("openCollectionDetails() - doesn't support action at this fragment_currentDestId: $currentDestId")
        }
        navHost.navigate(action)

    }

    protected fun setupMenuProvider(topicId: String? = null, collectionId: String? = null) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.auto_wall_paper_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.auto_wallpaper_menu -> {
                            PreferenceUtils.setAutoWallpaperStatus(requireContext(), AUTO_SET_WALLPAPER_KEY, 1)
                            AutoSetWallpaperWorker.enqueueSetAutoWallpaper(
                                requireContext(),
                                collectionId = collectionId,
                                topicId = topicId
                            )
                            true
                        }

                        R.id.cancel_set_wallpaper_menu -> {
                            PreferenceUtils.setAutoWallpaperStatus(requireContext(), AUTO_SET_WALLPAPER_KEY, 0)
                            AutoSetWallpaperWorker.cancelAutoWallpaperWorker(requireContext())
                            true
                        }

                        R.id.wallpaper_history -> {
                            openWallpaperHistoryFragment()
                            true
                        }

                        else -> false
                    }
                }

                override fun onPrepareMenu(menu: Menu) {
                    super.onPrepareMenu(menu)
                    val currentStatus = PreferenceUtils.getAutoWallpaperStatus(requireContext(), AUTO_SET_WALLPAPER_KEY)
                    menu.findItem(getAutoWallpaperMenuItem(currentStatus)).isChecked = true
                }
            },
            viewLifecycleOwner, Lifecycle.State.STARTED
        )
    }

    private fun openWallpaperHistoryFragment() {
        val navHost = findNavController()
        val action = when (val currentDestId = navHost.currentDestination?.id) {
            R.id.collection_details_fragment_dest -> CollectionDetailsFragmentDirections.actionCollectionDetailsFragmentToHistoryWallpaperFragment()
            R.id.topic_details_fragment_dest -> TopicDetailsFragmentDirections.actionTopicDetailsFragmentToHistoryWallpaperFragment()
            else -> error("openWallpaperHistoryFragment() - doesn't support action at this fragment_currentDestId: $currentDestId")
        }
        navHost.navigate(action)
    }

    private fun getAutoWallpaperMenuItem(status: Int): Int = when (status) {
        0 -> R.id.cancel_set_wallpaper_menu
        1 -> R.id.auto_wallpaper_menu
        else -> error("invalid type")
    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }
}