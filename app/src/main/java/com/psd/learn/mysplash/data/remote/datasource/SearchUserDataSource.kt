package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.remote.entity.SearchUserResponseItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchUserDataSource(
    private val unSplashApiService: UnSplashApiService,
    queryText: String?
) : AbsPagingDataSource<UserItem>(queryText) {

    override suspend fun getListDataPaging(
        queryText: String?,
        page: Int,
        perPage: Int
    ): List<UserItem> {

        if (queryText == null) return emptyList()
        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getSearchUserResult(
                query = queryText,
                page = page,
                perPage = perPage
            )
        }

        return response.results.map { it.toUserItem() }
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