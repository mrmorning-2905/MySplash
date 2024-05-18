package com.psd.learn.mysplash.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.ViewModelFactory
import com.psd.learn.mysplash.databinding.SearchFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.search.collections.SearchCollectionListFragment
import com.psd.learn.mysplash.ui.search.photos.SearchPhotoListFragment
import com.psd.learn.mysplash.ui.search.users.SearchUserListFragment
import com.psd.learn.mysplash.ui.viewmodels.SearchPhotoViewModel

class SearchFragment : BaseFragment<SearchFragmentLayoutBinding>(SearchFragmentLayoutBinding::inflate) {
    private val viewModel by activityViewModels<SearchPhotoViewModel> { ViewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupSearchChange()
    }

    private fun setupViewPager() {
        binding.viewPager.run {
            adapter = SearchViewPagerAdapter(this@SearchFragment)
            TabLayoutMediator(binding.tabLayout, this) {tab, position ->
                tab.setIcon(TAB_ICON_DRAWABLES[position])
                tab.text = TAB_TITLES[position]
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

    companion object {
        private val TAB_ICON_DRAWABLES = arrayOf(R.drawable.photo_tab_icon, R.drawable.collections_tab_icon, R.drawable.tab_user_icon)
        private val TAB_TITLES = arrayOf("Photos", "Collections", "Users")
    }
}