package com.psd.learn.mysplash.ui.search.users

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.ViewModelFactory
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.SearchUserFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseListFragment
import com.psd.learn.mysplash.ui.utils.debounce
import com.psd.learn.mysplash.ui.viewmodels.SearchUserListViewModel
import com.psd.learn.mysplash.ui.viewmodels.SearchViewModel

class SearchUserListFragment :
    BaseListFragment<UserItem, SearchUserFragmentLayoutBinding>(inflate = SearchUserFragmentLayoutBinding::inflate) {
    private val mainSearchViewModel by activityViewModels<SearchViewModel> { ViewModelFactory }

    private val searchUserViewModel by viewModels<SearchUserListViewModel> { ViewModelFactory }

    private val searUserListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SearchUserListAdapter(
            requestManager = Glide.with(this@SearchUserListFragment),
            onItemClickListener = { userItem -> showMessageToast("clicked on user profile have id: ${userItem.userId}") }
        )
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = SearchUserListFragment()
    }

    override fun submitList(items: List<UserItem>) {
        searUserListAdapter.submitList(items)
    }

    override fun setupView() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = searUserListAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupViewModel() {
        mainSearchViewModel.queryLiveData
            .debounce(650L, searchUserViewModel.viewModelScope)
            .distinctUntilChanged()
            .observe(viewLifecycleOwner) { queryText ->
                if (queryText.isNotEmpty()) {
                    searchUserViewModel.loadFirstPage(queryText)
                    handleLoadMorePage(queryText, binding.recyclerView, searchUserViewModel)
                }
            }

        searchUserViewModel.uiStateLiveData.observe(viewLifecycleOwner) { uiState ->
            renderUiState(uiState, binding.progressBar)
        }

        searchUserViewModel.result.observe(viewLifecycleOwner) { totalResult ->
            binding.searchResult.text = "Result: $totalResult users"
        }
    }

    override fun onDestroyView() {
        Log.d("sangpd", "onDestroyView: SearchPhotoListFragment")
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }
}