package com.psd.learn.mysplash.ui.feed.collections

import com.psd.learn.mysplash.databinding.CollectionsFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment

class CollectionsFragment : BaseFragment<CollectionsFragmentLayoutBinding>(inflate = CollectionsFragmentLayoutBinding::inflate) {

    companion object {
        fun newInstance() = CollectionsFragment()
    }
}