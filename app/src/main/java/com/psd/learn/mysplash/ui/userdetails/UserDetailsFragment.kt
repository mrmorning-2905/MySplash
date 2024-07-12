package com.psd.learn.mysplash.ui.userdetails

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
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.UserDetailsLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.feed.photos.details.InfoGridAdapter
import com.psd.learn.mysplash.ui.feed.photos.details.InfoModel
import com.psd.learn.mysplash.ui.userdetails.collections.UserDetailsCollectionsFragment
import com.psd.learn.mysplash.ui.userdetails.likes.UserDetailsLikedPhotosFragment
import com.psd.learn.mysplash.ui.userdetails.photos.UserDetailPhotosFragment
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
                binViewPager(userDetailsItem)
            }
            else -> {}
        }
    }

    private fun getTabTileList(userDetails: UserItem): ArrayList<String> {
        val titleArr = arrayListOf<String>()
        if (userDetails.totalPhotos > 0) titleArr.add("Photos")
        if (userDetails.totalCollections > 0) titleArr.add("Collections")
        if (userDetails.totalLikes > 0) titleArr.add("Liked")
        return titleArr
    }

    private fun bindProfile(userDetails: UserItem) {
        binding.run {
            userProfileImage.loadProfilePicture(
                requestManager = Glide.with(this@UserDetailsFragment),
                url = userDetails.profileUrl
            )
            locationContainer.visibility = View.VISIBLE
            bioContainer.visibility = View.VISIBLE
            userName.text = userDetails.userNameDisplay
            userAddress.text = userDetails.location
            bio.text = userDetails.bio
        }
    }

    private fun bindUserInfoGridView(userDetails: UserItem) {
        val infoList = arrayListOf(
            InfoModel(userDetails.totalPhotos.toString(), "Photos"),
            InfoModel(userDetails.totalCollections.toString(), "Collections"),
            InfoModel(userDetails.totalLikes.toString(), "Liked"),
        )
        val infoGridViewAdapter = InfoGridAdapter(requireContext(), infoList)
        binding.infoGridview.adapter = infoGridViewAdapter
    }

    private fun binViewPager(userDetails: UserItem) {
        val viewPagerAdapter = UserDetailsViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, userDetails)
        setupViewPager(
            viewPager = binding.viewPager,
            tabLayout = binding.tabLayout,
            pagerAdapter = viewPagerAdapter,
            tabTitleArr = getTabTileList(userDetails)
        )
    }

    inner class UserDetailsViewPagerAdapter(
        fragment: FragmentManager,
        lifecycle: Lifecycle,
        private val userDetails: UserItem
    ) : FragmentStateAdapter(fragment, lifecycle) {

        private val titleList: ArrayList<String> by lazy { getTabTileList(userDetails) }
        override fun getItemCount(): Int = titleList.size

        override fun createFragment(position: Int): Fragment {
            return when (titleList[position]) {
                "Photos" -> UserDetailPhotosFragment.newInstance()
                "Collections" -> UserDetailsCollectionsFragment.newInstance()
                "Liked" -> UserDetailsLikedPhotosFragment.newInstance()
                else -> error("Invalid position: $position")
            }
        }
    }
}