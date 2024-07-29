package com.psd.learn.mysplash.ui.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.databinding.LoadStateFooterLayoutBinding
import com.psd.learn.mysplash.ui.utils.safeHandleClickListener

class PagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bindView(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent, retry)
    }

    inner class LoadStateViewHolder(
        parent: ViewGroup,
        retryClickListener: () -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.load_state_footer_layout, parent, false)
    ) {

        private val binding = LoadStateFooterLayoutBinding.bind(itemView)
        init {
            binding.retryButton.safeHandleClickListener { retryClickListener() }
        }

        fun bindView(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}