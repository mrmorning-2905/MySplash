package com.psd.learn.mysplash.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.psd.learn.mysplash.MainViewModel
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.SortByType
import com.psd.learn.mysplash.databinding.FeedFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.feed.collections.CollectionsListFragment
import com.psd.learn.mysplash.ui.feed.photos.PhotosListFragment
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotosListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment :
    BaseFragment<FeedFragmentLayoutBinding>(inflate = FeedFragmentLayoutBinding::inflate) {

    private val mainViewModel by activityViewModels<MainViewModel>()

    private var popupMenu: PopupMenu? = null

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
        val viewPagerAdapter = FeedViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        setupViewPager(
            viewPager = binding.viewPager,
            tabLayout = binding.tabLayout,
            pagerAdapter = viewPagerAdapter,
            tabTitleArr = FEED_TAB_TITLES
        )
        setupMenuBottomBar()
        observerBottomLayout()
    }

    private fun observerBottomLayout() {
        mainViewModel.isShowBottomMenu.observe(viewLifecycleOwner) { isShow ->
            binding.run {
                bottomAppbar.visibility = if (isShow) View.GONE else View.VISIBLE
                addFabBtn.visibility = if (isShow) View.GONE else View.VISIBLE
            }
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
                    createPopupMenu(requireActivity().findViewById(R.id.sort_by_menu))
                    true
                }

                else -> false
            }
        }
    }

    private fun createPopupMenu(anchorView: View?) {
        if (anchorView == null) {
            Log.d("sangpd", "createPopupMenu() - anchorView is null")
            return
        }
        popupMenu = PopupMenu(requireContext(), anchorView, Gravity.END, 0, R.style.popupOverflowMenu).apply {
            inflate(R.menu.sort_by_menu)
            setOnDismissListener { popupMenu = null }
            menu.findItem(R.id.sort_by_latest).isChecked = true
            setOnMenuItemClickListener { menuItem ->
                menuItem.isChecked = true
                true
            }
        }
        popupMenu?.show()
    }

    private fun getSortByTypeMenuItem(type: Int): Int = when (type) {
        SortByType.LATEST_TYPE -> R.id.sort_by_latest
        SortByType.OLDEST_TYPE -> R.id.sort_by_oldest
        SortByType.POPULAR_TYPE -> R.id.sort_by_popular
        else -> error("invalid type")
    }

    private fun gotoSearchFragment() {
        val action = FeedFragmentDirections.actionFeedFragmentToSearchFragment(null)
        findNavController().navigate(action)
    }

    private class FeedViewPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragment, lifecycle) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> PhotosListFragment.newInstance()
                1 -> CollectionsListFragment.newInstance()
                2 -> FavoritePhotosListFragment.newInstance()
                else -> error("Invalid position: $position")
            }
        }
    }

    companion object {
        private val FEED_TAB_TITLES = arrayListOf("Photos", "Collections", "Favorites")
    }
}