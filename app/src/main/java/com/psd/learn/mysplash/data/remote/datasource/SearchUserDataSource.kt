package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.local.entity.toUserItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchUserDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
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
        return withContext(coroutineDispatcher) {
            val response = unSplashApiService.getSearchUserResult(
                query = query,
                page = page,
                perPage = perPage
            )
            totalResult(response.total)

            response.results.map { it.toUserItem() }
            //todo find other solution because it can be call many api --> limit call number is 50 times/hour
            /*.map { userItem ->
                val userPhotoList =
                    unSplashApiService.getUserPhotos(
                        userItem.userNameAccount, 1, 10, orderBy = "views"
                    )
                        .map {
                            it.toPhotoItem()
                        }
                userItem.copy(photoList = userPhotoList)
            }*/
        }
    }
}