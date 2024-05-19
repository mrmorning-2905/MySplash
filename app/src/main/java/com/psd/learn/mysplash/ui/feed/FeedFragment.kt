package com.psd.learn.mysplash.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.databinding.FeedFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.feed.collections.CollectionsListFragment
import com.psd.learn.mysplash.ui.feed.photos.PhotosListFragment
import com.psd.learn.mysplash.ui.search.SearchFragment
import com.psd.learn.mysplash.ui.utils.TAB_ICON_DRAWABLES
import com.psd.learn.mysplash.ui.utils.TAB_TITLES
import com.psd.learn.mysplash.ui.widget.CustomTabViewHolder
import com.psd.learn.mysplash.ui.widget.TabItem

class FeedFragment :
    BaseFragment<FeedFragmentLayoutBinding>(inflate = FeedFragmentLayoutBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupMenuBottomBar()
    }

    private fun setupViewPager() {
        binding.viewPager.run {
            adapter = FeedViewPagerAdapter(this@FeedFragment)

            TabLayoutMediator(binding.tabLayout, this) { tab, position ->
                tab.apply {
                    customView = CustomTabViewHolder(context).also {
                        val tabItem = TabItem(TAB_TITLES[position], TAB_ICON_DRAWABLES[position])
                        it.bind(tabItem)
                    }
                }
            }.attach()
        }
    }

    private fun setupMenuBottomBar() {
        binding.bottomAppbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_menu -> {
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        addToBackStack(null)
                        replace<SearchFragment>(
                            containerViewId = R.id.fragment_container_view,
                            tag = SearchFragment::class.java.simpleName
                        )
                    }
                    true
                }

                R.id.sort_by_menu -> {
                    false
                }

                else -> false
            }
        }
    }

    private class FeedViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> PhotosListFragment.newInstance()
                1 -> CollectionsListFragment.newInstance()
                else -> error("Invalid position: $position")
            }
        }
    }
}