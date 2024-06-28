package com.psd.learn.mysplash.data.local.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.psd.learn.mysplash.START_PAGE_INDEX
import com.psd.learn.mysplash.data.local.dao.PhotosDao
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.utils.log.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotosLocalPagingSource(
    private val photosDao: PhotosDao
) : PagingSource<Int, PhotoItem>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoItem>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            val resultPage = state.closestPageToPosition(anchorPos)
            resultPage?.prevKey?.plus(1)
                ?: resultPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoItem> {
        val position = params.key ?: START_PAGE_INDEX
        return try {
            val photosList = withContext(Dispatchers.IO) {
                photosDao.getPhotosWithLimit(position)
            }
            val nextKey = if (photosList.isEmpty()) null else (position + 1)
            LoadResult.Page(
                data = photosList,
                prevKey = if (position == START_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Logger.e("sangpd", "load() - exception: $e")
            LoadResult.Error(e)
        }
    }
}