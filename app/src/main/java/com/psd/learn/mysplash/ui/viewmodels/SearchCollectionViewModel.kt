package com.psd.learn.mysplash.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.remote.entity.SearchCollectionResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService

class SearchCollectionViewModel(
    private val unSplashApiService: UnSplashApiService
) : AbsListItemViewModel<CollectionItem>(){

    private val _results = MutableLiveData(0)
    val result get() = _results


    override suspend fun getListItems(
        searchText: String,
        currentPage: Int,
        itemPerPage: Int
    ): List<CollectionItem> {
    Log.d("sangpd", "SearchCollectionViewModel_getListItems_searchText: $searchText")
        val searchResult = unSplashApiService.getSearchCollectionResult(
            query = searchText,
            page = currentPage,
            perPage = itemPerPage
        )
        _results.value = searchResult.total
        return searchResult.results.map { it.toCollectionItem() }
    }
}

private fun SearchCollectionResponseItem.Result.toCollectionItem(): CollectionItem {
    return CollectionItem(
        id = id,
        userName = user.name,
        userProfileUrl = user.profileImage.medium,
        coverPhotoUrl = coverPhoto.urls.regular,
        coverDescription = title,
        numberImages = totalPhotos,
        userId = user.id
    )
}

