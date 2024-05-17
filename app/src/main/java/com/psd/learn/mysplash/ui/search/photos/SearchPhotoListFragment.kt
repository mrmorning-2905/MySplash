package com.psd.learn.mysplash.ui.search.photos

import com.psd.learn.mysplash.databinding.SearchPhotoFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment

class SearchPhotoListFragment : BaseFragment<SearchPhotoFragmentLayoutBinding>(inflate = SearchPhotoFragmentLayoutBinding::inflate) {

    companion object{
        fun newInstance() = SearchPhotoListFragment()
    }
}