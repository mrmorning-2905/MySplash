package com.psd.learn.mysplash.ui.search.users

import androidx.lifecycle.MutableLiveData
import com.psd.learn.mysplash.AbsListItemViewModel
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.remote.entity.SearchUserResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchUserListViewModel @Inject constructor(
    private val unSplashApiService: UnSplashApiService
) : AbsListItemViewModel<UserItem>(){

    private val _results = MutableLiveData(0)
    val result get() = _results
    override suspend fun getListItems(
        searchText: String,
        currentPage: Int,
        itemPerPage: Int
    ): List<UserItem> {
        val searchResult = unSplashApiService.getSearchUserResult(
            query = searchText,
            page = currentPage,
            perPage = itemPerPage
        )

        _results.value = searchResult.total

        return searchResult.results.map { it.toUserItem() }
    }
}

private fun SearchUserResponseItem.Result.toUserItem(): UserItem {
    return UserItem(
        userId = id,
        profileUrl = profileImage.medium,
        userName = name,
        userInfo = twitterUsername ?: ""
    )
}