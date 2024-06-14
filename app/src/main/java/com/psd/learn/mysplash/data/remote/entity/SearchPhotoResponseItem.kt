package com.psd.learn.mysplash.data.remote.entity

import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class SearchPhotoResponseItem(
    @Json(name = "results")
    val results: List<PhotoResponseItem>,
    @Json(name = "total")
    val total: Int, // 10000
    @Json(name = "total_pages")
    val totalPages: Int // 1000
)