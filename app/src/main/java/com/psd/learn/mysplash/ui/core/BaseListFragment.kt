package com.psd.learn.mysplash.ui.core

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.AbsListItemViewModel

abstract class BaseListFragment<T, VB: ViewBinding>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment<VB>(inflate) {

    protected var gridLayoutManager: GridLayoutManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
        initAdapter()
        setupViewModel()
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

    protected fun renderUiState(uiState: UiState<T>, progressBar: View) {
        when (uiState) {
            is UiState.Content -> {
                setViewVisible(progressBar, uiState.nextPageState is UiState.NextPageState.Loading)
                submitList(uiState.items)
            }

            is UiState.FirstPageError, UiState.FirstPageIdle -> {
                setViewVisible(progressBar, false)
                submitList(emptyList())
            }

            is UiState.FirstPageLoading -> {
                setViewVisible(progressBar, true)
                submitList(emptyList())
            }
        }
    }

    protected fun handleLoadMorePage(searchText: String, recyclerView: RecyclerView, viewModel: AbsListItemViewModel<T>) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0
                    && layoutManager.findLastVisibleItemPosition() + VISIBLE_THRESHOLD >= layoutManager.itemCount) {
                    viewModel.loadNextPage(searchText)
                }
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        gridLayoutManager?.let {
            it.spanCount = resources.getInteger(R.integer.grid_column_count)
        }
    }

    private fun setViewVisible(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    abstract fun submitList(items: List<T>)
    abstract fun initAdapter()
    abstract fun setupViewModel()

    companion object {
        private const val VISIBLE_THRESHOLD = 3
    }
}