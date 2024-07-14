package com.psd.learn.mysplash.ui.feed.photos.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.PhotoDetailsFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.core.UserArgs
import com.psd.learn.mysplash.ui.utils.ResultState
import com.psd.learn.mysplash.ui.utils.loadCoverThumbnail
import com.psd.learn.mysplash.ui.utils.loadProfilePicture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoDetailsFragment :
    BaseFragment<PhotoDetailsFragmentLayoutBinding>(inflate = PhotoDetailsFragmentLayoutBinding::inflate) {

    private val photoDetailsViewModel by viewModels<PhotoDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar(true, "", true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            photoDetailsViewModel.photoDetailsResult
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collect { resultState ->
                    renderUiState(resultState)
                }
        }
    }

    private fun renderUiState(state: ResultState<PhotoItem>) {
        setVisibleView(state is ResultState.Loading)
        when (state) {
            is ResultState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is ResultState.Success -> {
                binding.progressBar.visibility = View.GONE
                val photoItem = state.data
                bindImageView(photoItem)
                bindCameraInfo(photoItem)
                bindPhotoInfo(photoItem)
                bindTagList(photoItem)
                bindFavoriteBtn(photoItem)
            }

            else -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun updateFavoriteBtn(isFavorite: Boolean) {
        val favoriteIcon = if (isFavorite) R.drawable.favorite_selected_icon else R.drawable.favorite_icon
        binding.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), favoriteIcon))
    }

    private fun bindFavoriteBtn(photoItem: PhotoItem) {
        lifecycleScope.launch {
            photoDetailsViewModel.observerLocalPhotoById()
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { localId ->
                    updateFavoriteBtn(localId != null)
                    binding.favoriteBtn.setOnClickListener {
                        executeFavorite(photoItem.copy(isFavorite = localId != null))
                    }
                }
        }
    }

    private fun setVisibleView(isLoading: Boolean) {
        val state = if (isLoading) View.GONE else View.VISIBLE
        binding.run {
            locationContainer.visibility = state
            userBehaviorContainer.visibility = state
            cameraInfoContainer.visibility = state
            imageInfoContainer.visibility = state
            tagContainer.visibility = state
        }
    }

    private fun bindCameraInfo(photoItem: PhotoItem) {
        val detailsInfoList = arrayListOf(
            InfoModel("Camera", photoItem.cameraName),
            InfoModel("Focal Length", photoItem.focalLength),
            InfoModel("ISO", photoItem.iso),
            InfoModel("Aperture", photoItem.aperture),
            InfoModel("Exposure Time", photoItem.exposureTime),
            InfoModel("Dimensions", "${photoItem.width} x ${photoItem.height}"),
        )
        val infoGridViewAdapter = InfoGridAdapter(requireContext(), detailsInfoList)
        binding.cameraInfoGridview.adapter = infoGridViewAdapter
    }

    private fun bindPhotoInfo(photoItem: PhotoItem) {
        val photoInfoList = arrayListOf(
            InfoModel("Views", photoItem.numberView.toString()),
            InfoModel("Downloads", photoItem.numberDownload.toString()),
            InfoModel("Likes", photoItem.numberLikes.toString()),
        )
        val infoGridViewAdapter = InfoGridAdapter(requireContext(), photoInfoList)
        binding.imageInfoGridview.adapter = infoGridViewAdapter
    }

    private fun bindTagList(photoItem: PhotoItem) {
        binding.tagRecycleView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = TagListAdapter { tag ->
                gotoSearchFragment(tag)
            }.apply { submitList(photoItem.tagSet.toList()) }
        }
    }

    private fun gotoSearchFragment(tagName: String) {
        val navController = findNavController()
        val action = PhotoDetailsFragmentDirections.actionPhotoDetailsToSearchFragment(queryText = tagName)
        navController.navigate(action)
    }

    private fun gotoUserDetailsFragment(userInfo: UserArgs) {
        val navController = findNavController()
        val action = PhotoDetailsFragmentDirections.actionPhotoDetailsFragmentToUserDetailsFragment(userInfoArgs = userInfo)
        navController.navigate(action)
    }

    private fun bindImageView(photoItem: PhotoItem) {
        val requestManager = Glide.with(this@PhotoDetailsFragment)
        binding.coverImage.loadCoverThumbnail(
            requestManager = requestManager,
            coverUrl = photoItem.coverPhotoUrl,
            thumbnailUrl = photoItem.coverThumbnailUrl,
            coverColor = photoItem.coverColor,
            centerCrop = true
        )

        binding.profileLayout.run {
            userProfile.loadProfilePicture(
                requestManager,
                photoItem.userProfileUrl
            )
            userName.text = photoItem.userNameDisplay
            userOwnerContainer.setOnClickListener {
                gotoUserDetailsFragment(
                    UserArgs(
                        photoItem.userId,
                        photoItem.userNameAccount,
                        photoItem.userNameDisplay
                    )
                )
            }
        }
        binding.address.text = photoItem.location
    }

    override fun onDestroyView() {
        binding.tagRecycleView.adapter = null
        binding.imageInfoGridview.adapter = null
        binding.cameraInfoGridview.adapter = null
        super.onDestroyView()
    }
}