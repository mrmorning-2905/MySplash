package com.psd.learn.mysplash.ui.feed.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.ui.core.BaseFragment
import com.psd.learn.mysplash.ui.core.UiState

abstract class BaseListFragment<VB: ViewBinding, T>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment<VB>(inflate) {

    fun renderUiState(uiState: UiState<T>)
}