package com.psd.learn.mysplash.data.remote.entity


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class TopicResponseItem(
    @Json(name = "cover_photo")
    val coverPhoto: PhotoResponseItem,
    @Json(name = "description")
    val description: String, // Discover the beauty and intricacies of tiny details with stunning, up-close shots.
    @Json(name = "ends_at")
    val endsAt: String?, // 2024-08-31T23:59:59Z
    @Json(name = "featured")
    val featured: Boolean, // true
    @Json(name = "id")
    val id: String, // vQtUUPIzp6c
    @Json(name = "links")
    val links: Links,
    @Json(name = "media_types")
    val mediaTypes: List<String>,
    @Json(name = "owners")
    val owners: List<UserResponseItem>,
    @Json(name = "preview_photos")
    val previewPhotos: List<PreviewPhoto>,
    @Json(name = "published_at")
    val publishedAt: String, // 2024-07-22T15:48:27Z
    @Json(name = "slug")
    val slug: String, // macro-moments
    @Json(name = "starts_at")
    val startsAt: String, // 2024-07-22T00:00:00Z
    @Json(name = "status")
    val status: String, // open
    @Json(name = "title")
    val title: String, // Macro Moments
    @Json(name = "total_photos")
    val totalPhotos: Int, // 395
    @Json(name = "updated_at")
    val updatedAt: String, // 2024-08-08T16:20:14Z
    @Json(name = "visibility")
    val visibility: String // featured
) {
    @Keep
    data class Links(
        @Json(name = "html")
        val html: String, // https://unsplash.com/t/macro-moments
        @Json(name = "photos")
        val photos: String, // https://api.unsplash.com/topics/macro-moments/photos
        @Json(name = "self")
        val self: String // https://api.unsplash.com/topics/macro-moments
    )
    @Keep
    data class PreviewPhoto(
        @Json(name = "asset_type")
        val assetType: String, // photo
        @Json(name = "blur_hash")
        val blurHash: String, // LD8gytbu9trYn6V@R*kC02nj-CXR
        @Json(name = "created_at")
        val createdAt: String, // 2019-06-27T00:16:44Z
        @Json(name = "id")
        val id: String, // MtRmdlbKYVA
        @Json(name = "slug")
        val slug: String, // pink-petaled-flower-plants-MtRmdlbKYVA
        @Json(name = "updated_at")
        val updatedAt: String, // 2024-08-09T03:25:22Z
        @Json(name = "urls")
        val urls: Urls
    ) {
        @Keep
        data class Urls(
            @Json(name = "full")
            val full: String, // https://images.unsplash.com/photo-1561594592-d4b0e1201a58?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb
            @Json(name = "raw")
            val raw: String, // https://images.unsplash.com/photo-1561594592-d4b0e1201a58?ixlib=rb-4.0.3
            @Json(name = "regular")
            val regular: String, // https://images.unsplash.com/photo-1561594592-d4b0e1201a58?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max
            @Json(name = "small")
            val small: String, // https://images.unsplash.com/photo-1561594592-d4b0e1201a58?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max
            @Json(name = "small_s3")
            val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1561594592-d4b0e1201a58
            @Json(name = "thumb")
            val thumb: String // https://images.unsplash.com/photo-1561594592-d4b0e1201a58?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max
        )
    }
}