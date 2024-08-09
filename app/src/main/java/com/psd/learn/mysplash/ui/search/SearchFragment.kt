package com.psd.learn.mysplash.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.psd.learn.mysplash.databinding.SearchFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.search.collections.SearchCollectionListFragment
import com.psd.learn.mysplash.ui.search.photos.SearchPhotoListFragment
import com.psd.learn.mysplash.ui.search.users.SearchUserListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchFragmentLayoutBinding>(SearchFragmentLayoutBinding::inflate) {

    private val tagName by lazy(LazyThreadSafetyMode.NONE) { navArgs<SearchFragmentArgs>().value.queryText }

    private val viewModel by activityViewModels<PagingSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar(false, "", false)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPagerAdapter = SearchViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        setupViewPager(
            viewPager = binding.viewPager,
            tabLayout = binding.tabLayout,
            pagerAdapter = viewPagerAdapter,
            tabTitleArr = SEARCH_TAB_TITLES
        ) {}
        setupSearchChange()
        setUpNavigation()
    }

    private fun setupSearchChange() {
        if (tagName != null) {
            binding.searchEditText.setText(tagName)
            viewModel.onApplyUserAction(SearchAction.Search(tagName))
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val queryText = s?.trim()?.toString() ?: ""
                if (queryText.isNotEmpty()) {
                    val searchAction = SearchAction.Search(queryText)
                    viewModel.onApplyUserAction(searchAction)
                }
            }
        })
    }

    private fun setUpNavigation() {
        binding.textInputQuery.setStartIconOnClickListener {
            findNavController().popBackStack()
        }
    }

    private class SearchViewPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragment, lifecycle) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> SearchPhotoListFragment.newInstance()
                1 -> SearchCollectionListFragment.newInstance()
                2 -> SearchUserListFragment.newInstance()
                else -> error("Invalid position: $position")
            }
        }
    }

    companion object {
        private val SEARCH_TAB_TITLES = arrayListOf("Photos", "Collections", "Users")
    }
}