package com.psd.learn.mysplash.ui.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.remote.datasource.UserDetailsDataSource
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
    private val userDetailsDataSource: UserDetailsDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userNameAccount = UserDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).userInfoArgs.userNameAccount

    val userDetailsStateFlow: StateFlow<ResultState<UserItem>> =
        flow {
            try {
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
}