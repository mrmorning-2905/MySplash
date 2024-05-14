package com.psd.learn.mysplash.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.ui.viewmodels.AbsListItemViewModel

abstract class BaseListFragment<T, VB: ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null

    protected val binding get() = _binding!!

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflate(inflater, container, false).also { _binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
    }

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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

    protected fun handleLoadMorePage(recyclerView: RecyclerView, viewModel: AbsListItemViewModel<T>) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0
                    && layoutManager.findLastVisibleItemPosition() + VISIBLE_THRESHOLD >= layoutManager.itemCount) {
                    viewModel.loadNextPage()
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