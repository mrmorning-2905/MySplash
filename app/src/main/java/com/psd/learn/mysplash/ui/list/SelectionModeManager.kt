package com.psd.learn.mysplash.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.ui.utils.updateValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectionModeManager @Inject constructor() : ViewModel() {

    private val _listPhotoItem = MutableLiveData<List<PhotoItem>>(ArrayList())
    val listPhotoItem: LiveData<List<PhotoItem>>
        get() = _listPhotoItem

    private val _isChoiceMode = MutableLiveData(false)
    val isChoiceMode: LiveData<Boolean>
        get() = _isChoiceMode

    private val _listPhotoItemChecked = MutableLiveData<HashSet<PhotoItem>>(HashSet())
    val listPhotoItemChecked: LiveData<HashSet<PhotoItem>>
        get() = _listPhotoItemChecked

    fun isSelectionMode(): Boolean = isChoiceMode.value ?: false

    fun enableSelectionMode() {
        _isChoiceMode.updateValue(true)
    }

    fun disableSelectionMode() {
        _isChoiceMode.updateValue(false)
        _listPhotoItemChecked.updateValue(HashSet())
    }

    fun isCheckedPhotoItem(item: PhotoItem): Boolean {
        return _listPhotoItemChecked.value?.contains(item) ?: false
    }

    fun getListItemChecked(): List<PhotoItem> = _listPhotoItemChecked.value?.toList() ?: emptyList()

    fun addCheckedPhotoItem(item: PhotoItem) {
        _listPhotoItemChecked.value?.let { checkedList ->
            if (checkedList.contains(item)) {
                checkedList.remove(item)
            } else {
                checkedList.add(item)
            }
            Log.d("sangpd", "addCheckedPhotoItem_checkedList: ${checkedList.size}")
            _listPhotoItemChecked.updateValue(checkedList)
        }
    }

    fun isAllPhotoChecked(): Boolean = _listPhotoItemChecked.value?.size == _listPhotoItem.value?.size

    fun isEmptyCheckablePhoto(): Boolean = _listPhotoItem.value?.isEmpty() ?: true

    fun checkAllPhotoItem(isCheckedAll: Boolean) {
        val setPhoto = if (isCheckedAll) {
            _listPhotoItem.value?.toHashSet() ?: HashSet()
        } else {
            HashSet()
        }
        _listPhotoItemChecked.updateValue(setPhoto)
    }
}