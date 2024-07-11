package com.psd.learn.mysplash.ui.userdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.psd.learn.mysplash.USER_DETAILS_PHOTOS_TYPE
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.remote.datasource.UserDetailsDataSource
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.ui.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    pagingRepository: UnSplashPagingRepository,
    private val userDetailsDataSource: UserDetailsDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userNameAccount = UserDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).userInfoArgs.userNameAccount

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
}