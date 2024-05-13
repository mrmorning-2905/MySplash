package com.psd.learn.mysplash.data.remote.repository

import com.psd.learn.mysplash.data.remote.entity.PhotoResponseItem
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface UnSplashApiService {

    @GET("photos")
    suspend fun getPhotoListOnFeed(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<PhotoResponseItem>

    companion object {
        operator fun invoke(retrofit: Retrofit): UnSplashApiService = retrofit.create()
    }
}