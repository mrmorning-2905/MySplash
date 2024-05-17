package com.psd.learn.mysplash.ui.feed.photos

import com.psd.learn.mysplash.databinding.PhotoDetailsFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment

class PhotoDetailsFragment : BaseFragment<PhotoDetailsFragmentLayoutBinding>(inflate = PhotoDetailsFragmentLayoutBinding::inflate) {

    companion object {
        fun newInstance() = PhotoDetailsFragment()
    }
}