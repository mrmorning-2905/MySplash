package com.psd.learn.mysplash.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.databinding.FeedFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.feed.collections.CollectionsFragment
import com.psd.learn.mysplash.ui.feed.photos.FeedPhotosFragment

class FeedFragment : BaseFragment<FeedFragmentLayoutBinding>(inflate = FeedFragmentLayoutBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.viewPager.run {
            adapter = FeedViewPagerAdapter(this@FeedFragment)
            TabLayoutMediator(binding.tabLayout, this) { tab, position ->
                tab.text = TAB_TITLES[position]
                tab.setIcon(TAB_ICON_DRAWABLES[position])
            }.attach()
        }
    }

    private class FeedViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
           return when(position) {
               0 -> FeedPhotosFragment.newInstance()
               1 -> CollectionsFragment.newInstance()
               else -> error("Invalid position: $position")
           }
        }
    }

    companion object {
        private val TAB_ICON_DRAWABLES = arrayOf(R.drawable.photo_tab_icon, R.drawable.collections_tab_icon)
        private val TAB_TITLES = arrayOf("Photos", "Collections")
    }
}