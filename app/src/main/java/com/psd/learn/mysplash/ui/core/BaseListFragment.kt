package com.psd.learn.mysplash.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.ui.viewmodels.AbsListItemViewModel

abstract class BaseListFragment<T, VB: ViewBinding>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment<VB>(inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
    }

    protected fun renderUiState(uiState: UiState<T>, progressBar: View) {
        when (uiState) {
            is UiState.Content -> {
                setViewVisible(progressBar, uiState.nextPageState is UiState.NextPageState.Loading)
                submitList(uiState.items)
            }

            is UiState.FirstPageError -> {
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

    private fun setViewVisible(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    abstract fun submitList(items: List<T>)
    abstract fun setupView()
    abstract fun setupViewModel()

    companion object {
        private const val VISIBLE_THRESHOLD = 3
    }
}