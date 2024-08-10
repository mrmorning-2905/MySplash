package com.psd.learn.mysplash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psd.learn.mysplash.ui.utils.updateValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _isShowBottomMenu = MutableLiveData(false)

    private val _tabPosition = MutableLiveData(0)

    val tabPosition: LiveData<Int>
        get() = _tabPosition

    val isShowBottomMenu: LiveData<Boolean>
        get() = _isShowBottomMenu

    fun showBottomMenu(value: Boolean) {
        _isShowBottomMenu.updateValue(value)
    }

    fun updateTabPosition(value: Int) {
        _tabPosition.updateValue(value)
    }
}