package com.psd.learn.mysplash.ui.feed.photos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
                    userProfile.loadProfilePicture(Glide.with(this@PhotoDetailsFragment), photoItem.userProfileUrl)
                    userName.text = photoItem.userName
                }
            }
            else -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun newInstance() = PhotoDetailsFragment()
    }
}