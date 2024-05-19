package com.psd.learn.mysplash.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.psd.learn.mysplash.ViewModelFactory
import com.psd.learn.mysplash.databinding.SearchFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.search.collections.SearchCollectionListFragment
import com.psd.learn.mysplash.ui.search.photos.SearchPhotoListFragment
import com.psd.learn.mysplash.ui.search.users.SearchUserListFragment
import com.psd.learn.mysplash.ui.utils.TAB_ICON_DRAWABLES
import com.psd.learn.mysplash.ui.utils.TAB_TITLES
import com.psd.learn.mysplash.ui.viewmodels.SearchViewModel
import com.psd.learn.mysplash.ui.widget.CustomTabViewHolder
import com.psd.learn.mysplash.ui.widget.TabItem

class SearchFragment : BaseFragment<SearchFragmentLayoutBinding>(SearchFragmentLayoutBinding::inflate) {
    private val viewModel by activityViewModels<SearchViewModel> { ViewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupSearchChange()
    }

    private fun setupViewPager() {
        binding.viewPager.run {
            adapter = SearchViewPagerAdapter(this@SearchFragment)
            TabLayoutMediator(binding.tabLayout, this) {tab, position ->
                tab.apply {
                    customView = CustomTabViewHolder(context).also {
                        val tabItem = TabItem(TAB_TITLES[position], TAB_ICON_DRAWABLES[position])
                        it.bind(tabItem)
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
                Log.d("sangpd", "afterTextChanged: ${s.toString()}")
                viewModel.textSearchChange(s.toString())
            }
        })
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