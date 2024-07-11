package com.psd.learn.mysplash.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.USER_DETAILS_TAB_TITLES
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.UserDetailsLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.feed.collections.CollectionsListFragment
import com.psd.learn.mysplash.ui.feed.photos.PhotosListFragment
import com.psd.learn.mysplash.ui.feed.photos.details.InfoGridAdapter
import com.psd.learn.mysplash.ui.feed.photos.details.InfoModel
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotosListFragment
import com.psd.learn.mysplash.ui.utils.ResultState
import com.psd.learn.mysplash.ui.utils.loadProfilePicture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsFragment : BaseFragment<UserDetailsLayoutBinding>(inflate = UserDetailsLayoutBinding::inflate) {

    private val userDetailViewModel by viewModels<UserDetailsViewModel>()

    private val args by lazy(LazyThreadSafetyMode.NONE) { navArgs<UserDetailsFragmentArgs>().value.userInfoArgs }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar(true, args.userNameDisplay, true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPagerAdapter = UserDetailsViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        setupViewPager(
            viewPager = binding.viewPager,
            tabLayout = binding.tabLayout,
            pagerAdapter = viewPagerAdapter,
            tabTitleArr = USER_DETAILS_TAB_TITLES
        )
        lifecycleScope.launch {
            userDetailViewModel.userDetailsStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collectLatest { resultState ->
                    renderUiState(resultState)
                }
        }
    }

    private fun renderUiState(uiState: ResultState<UserItem>) {
        when (uiState) {
            is ResultState.Success -> {
                val userDetailsItem = uiState.data
                bindProfile(userDetailsItem)
                bindUserInfoGridView(userDetailsItem)
            }

            else -> {}
        }
    }

    private fun bindProfile(userDetails: UserItem) {
        binding.run {
            userProfileImage.loadProfilePicture(
                requestManager = Glide.with(this@UserDetailsFragment),
                url = userDetails.profileUrl
            )
            userName.text = userDetails.userNameDisplay
            userAddress.text = userDetails.location
            bio.text = userDetails.bio
        }
    }

    private fun bindUserInfoGridView(userDetails: UserItem) {
        val infoList = arrayListOf(
            InfoModel(userDetails.totalPhotos.toString(), "Photos"),
            InfoModel(userDetails.totalCollections.toString(), "Collections"),
            InfoModel(userDetails.totalLikes.toString(), "Likes"),
        )
        val infoGridViewAdapter = InfoGridAdapter(requireContext(), infoList)
        binding.infoGridview.adapter = infoGridViewAdapter
    }

    private class UserDetailsViewPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
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
}