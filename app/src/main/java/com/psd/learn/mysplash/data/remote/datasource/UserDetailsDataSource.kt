package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.data.local.entity.toUserItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserDetailsDataSource(
    private val apiService: UnSplashApiService,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getResultUserDetailsInfo(userName: String): Result<UserItem> {
        return runSuspendCatching(dispatcher) {
            apiService.getUserDetails(userName).toUserItem()
        }
    }
}