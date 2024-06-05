package com.psd.learn.mysplash.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private val _queryLiveData = MutableLiveData("")
    val queryLiveData get() = _queryLiveData

    fun textSearchChange(text: String) {
        _queryLiveData.value = text
    }

    override fun onCleared() {
        super.onCleared()
        _queryLiveData.value = ""
    }
}