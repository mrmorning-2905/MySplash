package com.psd.learn.mysplash.data.remote.repository

import com.psd.learn.mysplash.data.remote.entity.CollectionResponseItem
import com.psd.learn.mysplash.data.remote.entity.PhotoResponseItem
import com.psd.learn.mysplash.data.remote.entity.SearchCollectionResponseItem
import com.psd.learn.mysplash.data.remote.entity.SearchPhotoResponseItem
import com.psd.learn.mysplash.data.remote.entity.SearchUserResponseItem
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnSplashApiService {

    @GET("photos")
    suspend fun getPhotoListOnFeed(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<PhotoResponseItem>

    @GET("collections")
    suspend fun getCollectionListOnFeed(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<CollectionResponseItem>

    @GET("collections/{id}/photos")
    suspend fun getPhotosOfCollection(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<PhotoResponseItem>

    @GET("photos/{id}")
    suspend fun getPhotoItem(
        @Path("id") id: String
    ): PhotoResponseItem


    @GET("search/photos")
    suspend fun getSearchPhotoResult(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchPhotoResponseItem

    @GET("search/collections")
    suspend fun getSearchCollectionResult(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchCollectionResponseItem

    @GET("search/users")
    suspend fun getSearchUserResult(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchUserResponseItem

    companion object {
        operator fun invoke(retrofit: Retrofit): UnSplashApiService = retrofit.create()
    }
}