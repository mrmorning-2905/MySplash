package com.psd.learn.mysplash.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.psd.learn.mysplash.databinding.SearchFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.search.collections.SearchCollectionListFragment
import com.psd.learn.mysplash.ui.search.photos.SearchPhotoListFragment
import com.psd.learn.mysplash.ui.search.users.SearchUserListFragment
import com.psd.learn.mysplash.TAB_TITLES
import com.psd.learn.mysplash.ui.widget.CustomTabViewHolder
import com.psd.learn.mysplash.ui.widget.TabItem
import com.psd.learn.mysplash.utils.log.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchFragmentLayoutBinding>(SearchFragmentLayoutBinding::inflate) {

    override val TAG: String
        get() = SearchFragment::class.java.simpleName

    private val searchText: String
        get() = arguments?.getString("KEY_WORD") ?: ""

    private val viewModel by activityViewModels<PagingSearchViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Logger.d(TAG, "searchText from tag: $searchText")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupSearchChange()
        setUpNavigation()
    }

    private fun setupViewPager() {
        binding.viewPager.run {
            adapter = SearchViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            with(binding.tabLayout) {
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        Logger.d(TAG, "setupViewPager() - selectedTab: ${tab?.position}")
                        (tab?.customView as? CustomTabViewHolder)
                            ?.bind { tabItemStatus = tabItemStatus.copy(isSelected = true) }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        (tab?.customView as? CustomTabViewHolder)
                            ?.bind { tabItemStatus = tabItemStatus.copy(isSelected = false) }
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }

                })
            }

            TabLayoutMediator(binding.tabLayout, this) { tab, position ->
                tab.apply {
                    customView = CustomTabViewHolder(context).apply {
                        tabItemStatus = TabItem(
                            text = TAB_TITLES[position],
                            isSelected = position == 0)
                    }
                }
            }.attach()
        }
    }

    private fun setupSearchChange() {
        if (searchText.isNotEmpty()) {
            binding.searchEditText.setText(searchText)
            viewModel.onApplyUserAction(SearchAction.Search(searchText))
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

    private class SearchViewPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragment, lifecycle) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> SearchPhotoListFragment.newInstance()
                1 -> SearchCollectionListFragment.newInstance()
                2 -> SearchUserListFragment.newInstance()
                else -> error("Invalid position: $position")
            }
        }

    }
}