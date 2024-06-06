package com.psd.learn.mysplash.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.psd.learn.mysplash.databinding.SearchFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.search.collections.SearchCollectionListFragment
import com.psd.learn.mysplash.ui.search.photos.SearchPhotoListFragment
import com.psd.learn.mysplash.ui.search.users.SearchUserListFragment
import com.psd.learn.mysplash.ui.utils.TAB_ICON_SELECTED_DRAWABLES
import com.psd.learn.mysplash.ui.utils.TAB_ICON_UNSELECTED_DRAWABLES
import com.psd.learn.mysplash.ui.utils.TAB_TITLES
import com.psd.learn.mysplash.ui.widget.CustomTabViewHolder
import com.psd.learn.mysplash.ui.widget.TabItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchFragmentLayoutBinding>(SearchFragmentLayoutBinding::inflate) {
    private val viewModel by activityViewModels<PagingSearchViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupSearchChange()
    }

    private fun setupViewPager() {
        binding.viewPager.run {
            adapter = SearchViewPagerAdapter(this@SearchFragment)
            with(binding.tabLayout) {
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
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
                            iconResUnselected = TAB_ICON_UNSELECTED_DRAWABLES[position],
                            iconResSelected = TAB_ICON_SELECTED_DRAWABLES[position],
                            isSelected = position == 0)
                    }
                }
            }.attach()
        }
    }

    private fun setupSearchChange() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateQueryTextSubmit(viewModel.userAction)
            }
        })
    }

    private fun updateQueryTextSubmit(onQueryChanged: (UiAction.Search) -> Unit) {
        binding.searchEditText.text?.trim()?.let {
            Log.d("sangpd", "updateQueryTextSubmit_query: $it")
            if (it.isNotEmpty()) {
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private class SearchViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
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