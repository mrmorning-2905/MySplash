package com.psd.learn.mysplash.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.remote.datasource.SearchCollectionDataSource
import com.psd.learn.mysplash.data.remote.datasource.SearchPhotoDataSource
import com.psd.learn.mysplash.data.remote.datasource.SearchUserDataSource
import com.psd.learn.mysplash.ui.utils.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnSplashPagingRepository @Inject constructor(
    private val unSplashApiService: UnSplashApiService
) {

    private val pageConfig = PagingConfig(
        pageSize = NETWORK_PAGE_SIZE,
        enablePlaceholders = false
    )

    fun getSearchPhotoResultStream(query: String?): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = {
                SearchPhotoDataSource(
                    unSplashApiService = unSplashApiService,
                    queryText = query
                )
            }
        ).flow
    }

    fun getSearchCollectionsResultStream(query: String?): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = {
                SearchCollectionDataSource(
                    unSplashApiService = unSplashApiService,
                    queryText = query
                )
            }
        ).flow
    }

    fun getSearchUsersResultStream(query: String?): Flow<PagingData<UserItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = {
                SearchUserDataSource(
                    unSplashApiService = unSplashApiService,
                    queryText = query
                )
            }
        ).flow
    }

}