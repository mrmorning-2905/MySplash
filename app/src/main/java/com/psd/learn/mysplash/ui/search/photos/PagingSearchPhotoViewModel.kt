package com.psd.learn.mysplash.ui.search.photos

import androidx.paging.PagingData
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.ui.search.PagingSearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PagingSearchPhotoViewModel @Inject constructor(
    private val pagingRepository: UnSplashPagingRepository
) : PagingSearchViewModel<PhotoItem>() {

    override fun getSearchItems(query: String?): Flow<PagingData<PhotoItem>> {
        return pagingRepository.getSearchPhotoResultStream(query)
    }
}