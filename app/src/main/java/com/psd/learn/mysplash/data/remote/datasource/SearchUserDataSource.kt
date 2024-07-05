package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.local.entity.toUserItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchUserDataSource(
    private val unSplashApiService: UnSplashApiService,
    queryText: String?,
    override val totalResult: (Int) -> Unit
) : AbsPagingDataSource<UserItem>(queryText) {

    override val TAG: String
        get() = SearchUserDataSource::class.java.simpleName
    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<UserItem> {

        if (query == null) return emptyList()
        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getSearchUserResult(
                query = query,
                page = page,
                perPage = perPage
            )
        }
        totalResult(response.total)
        return response.results.map { it.toUserItem() }
    }
}