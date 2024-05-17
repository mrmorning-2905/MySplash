package com.psd.learn.mysplash.ui.search.users

import com.psd.learn.mysplash.databinding.SearchUserFragmentLayoutBinding
import com.psd.learn.mysplash.ui.core.BaseFragment

class SearchUserListFragment : BaseFragment<SearchUserFragmentLayoutBinding>(inflate = SearchUserFragmentLayoutBinding::inflate) {

    companion object{
        fun newInstance() = SearchUserListFragment()
    }
}