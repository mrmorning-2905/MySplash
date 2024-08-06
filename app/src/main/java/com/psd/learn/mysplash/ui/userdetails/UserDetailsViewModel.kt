package com.psd.learn.mysplash.ui.userdetails

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.psd.learn.mysplash.USER_DETAILS_COLLECTIONS_TYPE
import com.psd.learn.mysplash.USER_DETAILS_LIKED_TYPE
import com.psd.learn.mysplash.USER_DETAILS_PHOTOS_TYPE
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.remote.datasource.UserDetailsDataSource
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotoHelper
import com.psd.learn.mysplash.ui.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val userDetailsDataSource: UserDetailsDataSource,
    pagingRepository: UnSplashPagingRepository,
    localRepository: PhotosLocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userNameAccount = UserDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).userInfoArgs.userNameAccount
    private val localPhotoIdStream by lazy { localRepository.observerLocalPhotoIdsStream() }

    val userDetailsStateFlow: StateFlow<ResultState<UserItem>> =
        flow {
            userDetailsDataSource.getResultUserDetailsInfo(userNameAccount)
                .onFailure { exception -> Log.d("sangpd", "UserDetailsViewModel_getResultUserDetailsInfo() failed: $exception") }
                .fold(
                    onSuccess = { userItem -> emit(ResultState.Success(userItem)) },
                    onFailure = { exception -> emit(ResultState.Error(exception)) }
                )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResultState.Loading
            )

    val userDetailsPhotosPagingData = pagingRepository
        .getUserDetailsPagingDataStream<PhotoItem>(userNameAccount, USER_DETAILS_PHOTOS_TYPE)
        .cachedIn(viewModelScope)
        .combine(localPhotoIdStream) { pagingData, localIdList ->
            pagingData.map { photoItem ->
                localIdList
                    .onFailure { error ->
                        Log.d(
                            "sangpd",
                            "UserDetailsViewModel_PhotosPagingData_observerLocalPhotoIds failed error: $error"
                        )
                    }
                    .fold(
                        onSuccess = { idList ->
                            val isFavorite = idList.contains(photoItem.photoId)
                            photoItem.copy(isFavorite = isFavorite)
                        },
                        onFailure = { photoItem }
                    )
            }
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(Dispatchers.Main)

    val userDetailsCollectionsPagingData = pagingRepository
        .getUserDetailsPagingDataStream<CollectionItem>(userNameAccount, USER_DETAILS_COLLECTIONS_TYPE)
        .cachedIn(viewModelScope)

    val userDetailsLikedPagingData = pagingRepository
        .getUserDetailsPagingDataStream<PhotoItem>(userNameAccount, USER_DETAILS_LIKED_TYPE)
        .cachedIn(viewModelScope)
        .combine(localPhotoIdStream) { pagingData, localIdList ->
            pagingData.map { photoItem ->
                localIdList
                    .onFailure { error ->
                        Log.d(
                            "sangpd",
                            "UserDetailsViewModel_LikedPagingData_observerLocalPhotoIds failed error: $error"
                        )
                    }
                    .fold(
                        onSuccess = { idList ->
                            val isFavorite = idList.contains(photoItem.photoId)
                            photoItem.copy(isFavorite = isFavorite)
                        },
                        onFailure = { photoItem }
                    )
            }
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(Dispatchers.Main)

    fun addOrRemoveFavoritePhotoItem(context: Context, photoItem: PhotoItem) {
        viewModelScope.launch {
            FavoritePhotoHelper.executeAddOrRemoveFavorite(context, photoItem)
        }
    }
}