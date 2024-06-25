package com.psd.learn.mysplash.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.TAB_TITLES
import com.psd.learn.mysplash.databinding.FeedFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.feed.collections.CollectionsListFragment
import com.psd.learn.mysplash.ui.feed.photos.PhotosListFragment
import com.psd.learn.mysplash.ui.widget.CustomTabViewHolder
import com.psd.learn.mysplash.ui.widget.TabItem
import com.psd.learn.mysplash.utils.log.Logger

class FeedFragment :
    BaseFragment<FeedFragmentLayoutBinding>(inflate = FeedFragmentLayoutBinding::inflate) {

    override val TAG: String
        get() = FeedFragment::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupMenuBottomBar()
    }

    private fun setupViewPager() {
        binding.viewPager.run {
            adapter = FeedViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

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

    private fun setupMenuBottomBar() {
        binding.bottomAppbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_menu -> {
                    gotoSearchFragment()
                    true
                }

                R.id.sort_by_menu -> {
                    //TODO handle sort menu
                    false
                }

                else -> false
            }
        }
    }

    private fun gotoSearchFragment() {
        val action = FeedFragmentDirections.actionFeedFragmentToSearchFragment()
        findNavController().navigate(action)
    }

    private class FeedViewPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragment, lifecycle) {
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