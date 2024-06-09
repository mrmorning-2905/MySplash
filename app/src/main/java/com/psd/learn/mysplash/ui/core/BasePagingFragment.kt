package com.psd.learn.mysplash.ui.core

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import com.psd.learn.mysplash.ui.search.UiAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BasePagingFragment<T : Any, VB : ViewBinding>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment<VB>(inflate) {

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
                    val scrollAction = UiAction.Scroll(currentQuery = uiState.value.query)
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
            adapter = pagingAdapter.withLoadStateHeaderAndFooter (
                header = PagingLoadStateAdapter { pagingAdapter.retry() },
                footer = PagingLoadStateAdapter { pagingAdapter.retry() }
            )
        }

        handleLoadState()
    }

    private fun handleLoadState() {
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
                setOnClickListener { pagingAdapter.refresh() }
            }
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        gridLayoutManager?.let {
            it.spanCount = resources.getInteger(R.integer.grid_column_count)
        }
    }

    protected open val mItemClickListener = object : OnItemClickListener {
        override fun coverPhotoClicked(photoId: String?) {
            showMessageToast("clicked on photo have id: $photoId")
        }

        override fun profileClicked(userId: String?) {
            showMessageToast("clicked on user profile have id: $userId")
        }
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }
}