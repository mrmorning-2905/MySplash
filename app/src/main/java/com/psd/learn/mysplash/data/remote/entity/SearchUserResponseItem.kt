package com.psd.learn.mysplash.data.remote.entity


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class SearchUserResponseItem(
    @Json(name = "results")
    val results: List<UserResponseItem>,
    @Json(name = "total")
    val total: Int, // 281
    @Json(name = "total_pages")
    val totalPages: Int // 29
)