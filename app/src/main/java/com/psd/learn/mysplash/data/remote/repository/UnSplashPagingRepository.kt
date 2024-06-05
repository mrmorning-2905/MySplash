package com.psd.learn.mysplash.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.datasource.SearchPhotoDataSource
import com.psd.learn.mysplash.ui.utils.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class UnSplashPagingRepository(
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

}