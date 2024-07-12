package com.psd.learn.mysplash.ui.userdetails

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
import com.psd.learn.mysplash.ui.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val userDetailsDataSource: UserDetailsDataSource,
    pagingRepository: UnSplashPagingRepository,
    localRepository: PhotosLocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userNameAccount = UserDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).userInfoArgs.userNameAccount
    private val localPhotoIdStream by lazy { localRepository.getPhotoIdsStream() }

    val userDetailsStateFlow: StateFlow<ResultState<UserItem>> =
        flow {
            try {
                Log.d("sangpd", "UserDetailsViewModel_userNameAccount: $userNameAccount")
                val userDetails = userDetailsDataSource.getUserDetailsInfo(userNameAccount)
                emit(ResultState.Success(userDetails))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emit(ResultState.Error(e))
            }
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
                val isFavorite = localIdList.contains(photoItem.photoId)
                photoItem.copy(isFavorite = isFavorite)
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
                val isFavorite = localIdList.contains(photoItem.photoId)
                photoItem.copy(isFavorite = isFavorite)
            }
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(Dispatchers.Main)
}