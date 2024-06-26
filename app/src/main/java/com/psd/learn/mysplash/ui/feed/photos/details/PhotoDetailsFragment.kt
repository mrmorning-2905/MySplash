package com.psd.learn.mysplash.ui.feed.photos.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.PhotoDetailsFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.utils.ResultState
import com.psd.learn.mysplash.ui.utils.loadCoverThumbnail
import com.psd.learn.mysplash.ui.utils.loadProfilePicture
import com.psd.learn.mysplash.utils.log.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoDetailsFragment :
    BaseFragment<PhotoDetailsFragmentLayoutBinding>(inflate = PhotoDetailsFragmentLayoutBinding::inflate) {

    private val photoDetailsViewModel by viewModels<PhotoDetailsViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val photoId = arguments?.getString("PHOTO_ID") ?: ""
        photoDetailsViewModel.emitPhotoId(photoId)
    }

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
                    Logger.d("sangpd", "state is: $resultState")
                    renderUiState(resultState)
                }
        }
    }

    private fun renderUiState(state: ResultState) {
        setVisibleView(state is ResultState.Loading)
        when (state) {
            is ResultState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is ResultState.Success<*> -> {
                binding.progressBar.visibility = View.GONE
                val photoItem = state.data as PhotoItem
                binding.coverImage.loadCoverThumbnail(
                    requestManager = Glide.with(this@PhotoDetailsFragment),
                    coverUrl = photoItem.coverPhotoUrl,
                    thumbnailUrl = photoItem.coverThumbnailUrl,
                    coverColor = photoItem.coverColor,
                    centerCrop = true
                )

                binding.profileLayout.run {
                    userProfile.loadProfilePicture(
                        Glide.with(this@PhotoDetailsFragment),
                        photoItem.userProfileUrl
                    )
                    userName.text = photoItem.userName
                }
                binding.address.text = photoItem.location
                bindCameraInfo(photoItem)
                bindPhotoInfo(photoItem)
                bindTagList(photoItem)
            }

            else -> {
                binding.progressBar.visibility = View.VISIBLE
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
        val infoGridViewAdapter = PhotoInfoGridAdapter(requireContext(), detailsInfoList)
        binding.cameraInfoGridview.adapter = infoGridViewAdapter
    }

    private fun bindPhotoInfo(photoItem: PhotoItem) {
        val photoInfoList = arrayListOf(
            InfoModel("Views", photoItem.numberView.toString()),
            InfoModel("Downloads", photoItem.numberDownload.toString()),
            InfoModel("Likes", photoItem.numberLikes.toString()),
        )
        val infoGridViewAdapter = PhotoInfoGridAdapter(requireContext(), photoInfoList)
        binding.imageInfoGridview.adapter = infoGridViewAdapter
    }

    private fun bindTagList(photoItem: PhotoItem) {
        binding.tagRecycleView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = TagListAdapter().apply { submitList(photoItem.tagList.toList()) }
        }
    }

    companion object {
        fun newInstance() = PhotoDetailsFragment()
    }
}