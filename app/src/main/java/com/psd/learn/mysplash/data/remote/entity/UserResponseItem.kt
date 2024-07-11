package com.psd.learn.mysplash.data.remote.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class UserResponseItem(
    @Json(name = "id")
    val id: String, // PN0aWWIssaM
    @Json(name = "updated_at")
    val updatedAt: String, // 2024-05-25T09:11:23Z
    @Json(name = "username")
    val username: String, // jakenackos
    @Json(name = "name")
    val name: String, // Jake Nackos
    @Json(name = "first_name")
    val firstName: String, // Jake
    @Json(name = "last_name")
    val lastName: String?, // Nackos
    @Json(name = "twitter_username")
    val twitterUsername: String?, // JakeNackos
    @Json(name = "portfolio_url")
    val portfolioUrl: String?, // http://jakenackos.com
    @Json(name = "bio")
    val bio: String?, // If you use any photos and would like to buy me a coffee as a "thankyou", you can here! 👉🏼 https://www.buymeacoffee.com/jakenackos or on Instagram: @jakenackos
    @Json(name = "location")
    val location: String?, // Salt Lake City, Utah
    @Json(name = "links")
    val links: Links,
    @Json(name = "profile_image")
    val profileImage: ProfileImage,
    @Json(name = "instagram_username")
    val instagramUsername: String?, // jakenackos
    @Json(name = "total_collections")
    val totalCollections: Int, // 17
    @Json(name = "total_likes")
    val totalLikes: Int, // 0
    @Json(name = "total_photos")
    val totalPhotos: Int, // 340
    @Json(name = "total_promoted_photos")
    val totalPromotedPhotos: Int, // 119
    @Json(name = "total_illustrations")
    val totalIllustrations: Int, // 0
    @Json(name = "total_promoted_illustrations")
    val totalPromotedIllustrations: Int, // 0
    @Json(name = "accepted_tos")
    val acceptedTos: Boolean, // true
    @Json(name = "for_hire")
    val forHire: Boolean, // true
    @Json(name = "social")
    val social: Social,
    @Json(name = "photos")
    val photos: List<PhotoResponseItem>?
) {
    @Keep
    data class Links(
        @Json(name = "self")
        val self: String, // https://api.unsplash.com/users/jakenackos
        @Json(name = "html")
        val html: String, // https://unsplash.com/@jakenackos
        @Json(name = "photos")
        val photos: String, // https://api.unsplash.com/users/jakenackos/photos
        @Json(name = "likes")
        val likes: String, // https://api.unsplash.com/users/jakenackos/likes
        @Json(name = "portfolio")
        val portfolio: String, // https://api.unsplash.com/users/jakenackos/portfolio
        @Json(name = "following")
        val following: String, // https://api.unsplash.com/users/jakenackos/following
        @Json(name = "followers")
        val followers: String // https://api.unsplash.com/users/jakenackos/followers
    )

    @Keep
    data class ProfileImage(
        @Json(name = "small")
        val small: String, // https://images.unsplash.com/profile-1574112923531-cdc4565ead61image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
        @Json(name = "medium")
        val medium: String, // https://images.unsplash.com/profile-1574112923531-cdc4565ead61image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
        @Json(name = "large")
        val large: String // https://images.unsplash.com/profile-1574112923531-cdc4565ead61image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
    )


    @Keep
    data class Social(
        @Json(name = "instagram_username")
        val instagramUsername: String?, // jakenackos
        @Json(name = "portfolio_url")
        val portfolioUrl: String?, // http://jakenackos.com
        @Json(name = "twitter_username")
        val twitterUsername: String?, // JakeNackos
    )
}