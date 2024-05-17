package com.psd.learn.mysplash.ui.search.collections

import com.psd.learn.mysplash.databinding.SearchCollectionFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment

class SearchCollectionListFragment : BaseFragment<SearchCollectionFragmentLayoutBinding>(inflate = SearchCollectionFragmentLayoutBinding::inflate) {

    companion object{
        fun newInstance() = SearchCollectionListFragment()
    }
}