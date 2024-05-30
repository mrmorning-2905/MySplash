package com.psd.learn.mysplash.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.entity.SearchPhotoResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchPhotoViewModel @Inject constructor(
    private val unSplashApiService: UnSplashApiService
) : AbsListItemViewModel<PhotoItem>() {

    private val _results = MutableLiveData(0)
    val result get() = _results

    override suspend fun getListItems(
        searchText: String,
        currentPage: Int,
        itemPerPage: Int
    ): List<PhotoItem> {
        Log.d("sangpd", "SearchPhotoViewModel_getListItems_searchText: $searchText")
        val searchResult = unSplashApiService.getSearchPhotoResult(
            query = searchText,
            page = currentPage,
            perPage = itemPerPage
        )
        _results.value = searchResult.total
        return searchResult.results.map { it.toPhotoItem() }
    }
}

private fun SearchPhotoResponseItem.Result.toPhotoItem(): PhotoItem {
    return PhotoItem(
        id = id,
        userName = user.name,
        userProfileUrl = user.profileImage.medium,
        coverPhotoUrl = urls.regular,
        photoDescription = altDescription,
        numberLikes = likes,
        userId = user.id
    )
}