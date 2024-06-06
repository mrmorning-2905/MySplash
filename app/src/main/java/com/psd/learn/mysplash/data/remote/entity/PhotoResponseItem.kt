package com.psd.learn.mysplash.data.remote.entity


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class PhotoResponseItem(
    @Json(name = "id")
    val id: String, // 6tv4nRMu2lY
    @Json(name = "slug")
    val slug: String, // a-large-library-filled-with-lots-of-books-6tv4nRMu2lY
    @Json(name = "created_at")
    val createdAt: String, // 2024-05-05T10:38:59Z
    @Json(name = "updated_at")
    val updatedAt: String, // 2024-05-09T23:55:30Z
    @Json(name = "promoted_at")
    val promotedAt: String? = null, // 2024-05-09T13:23:36Z
    @Json(name = "width")
    val width: Int, // 5324
    @Json(name = "height")
    val height: Int, // 8000
    @Json(name = "color")
    val color: String, // #260c0c
    @Json(name = "blur_hash")
    val blurHash: String? = null, // LLG958tR9Fnh%hWBozWB.mWCMxRj
    @Json(name = "description")
    val description: String?, // Instagram - @kaprion
    @Json(name = "alt_description")
    val altDescription: String? = null, // a large library filled with lots of books
    @Json(name = "breadcrumbs")
    val breadcrumbs: List<Any>,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "links")
    val links: Links,
    @Json(name = "likes")
    val likes: Int, // 96
    @Json(name = "liked_by_user")
    val likedByUser: Boolean, // false
    @Json(name = "current_user_collections")
    val currentUserCollections: List<Any>,
    @Json(name = "sponsorship")
    val sponsorship: Any?, // null
    @Json(name = "asset_type")
    val assetType: String, // photo
    @Json(name = "user")
    val user: User
) {

    @Keep
    data class Urls(
        @Json(name = "raw")
        val raw: String, // https://images.unsplash.com/photo-1714905532906-0b9ec1b22dfa?ixid=M3w2MDE2MDl8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNTMwMzEwOHw&ixlib=rb-4.0.3
        @Json(name = "full")
        val full: String, // https://images.unsplash.com/photo-1714905532906-0b9ec1b22dfa?crop=entropy&cs=srgb&fm=jpg&ixid=M3w2MDE2MDl8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNTMwMzEwOHw&ixlib=rb-4.0.3&q=85
        @Json(name = "regular")
        val regular: String, // https://images.unsplash.com/photo-1714905532906-0b9ec1b22dfa?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MDE2MDl8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNTMwMzEwOHw&ixlib=rb-4.0.3&q=80&w=1080
        @Json(name = "small")
        val small: String, // https://images.unsplash.com/photo-1714905532906-0b9ec1b22dfa?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MDE2MDl8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNTMwMzEwOHw&ixlib=rb-4.0.3&q=80&w=400
        @Json(name = "thumb")
        val thumb: String, // https://images.unsplash.com/photo-1714905532906-0b9ec1b22dfa?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MDE2MDl8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNTMwMzEwOHw&ixlib=rb-4.0.3&q=80&w=200
        @Json(name = "small_s3")
        val smallS3: String // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1714905532906-0b9ec1b22dfa
    )

    @Keep
    data class Links(
        @Json(name = "self")
        val self: String, // https://api.unsplash.com/photos/a-large-library-filled-with-lots-of-books-6tv4nRMu2lY
        @Json(name = "html")
        val html: String, // https://unsplash.com/photos/a-large-library-filled-with-lots-of-books-6tv4nRMu2lY
        @Json(name = "download")
        val download: String, // https://unsplash.com/photos/6tv4nRMu2lY/download?ixid=M3w2MDE2MDl8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNTMwMzEwOHw
        @Json(name = "download_location")
        val downloadLocation: String // https://api.unsplash.com/photos/6tv4nRMu2lY/download?ixid=M3w2MDE2MDl8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNTMwMzEwOHw
    )

    @Keep
    data class User(
        @Json(name = "id")
        val id: String, // CdtXrQZBtRA
        @Json(name = "updated_at")
        val updatedAt: String, // 2024-05-09T16:42:09Z
        @Json(name = "username")
        val username: String, // mbuff
        @Json(name = "name")
        val name: String, // Sung Jin Cho
        @Json(name = "first_name")
        val firstName: String, // Sung Jin
        @Json(name = "last_name")
        val lastName: String?, // Cho
        @Json(name = "twitter_username")
        val twitterUsername: String?, // Josh_Hild
        @Json(name = "portfolio_url")
        val portfolioUrl: String?, // http://ventiviews.com
        @Json(name = "bio")
        val bio: String?, // My Favorite Moments...
        @Json(name = "location")
        val location: String?, // Seoul, Korea
        @Json(name = "links")
        val links: Links,
        @Json(name = "profile_image")
        val profileImage: ProfileImage,
        @Json(name = "instagram_username")
        val instagramUsername: String?, // ventiviews
        @Json(name = "total_collections")
        val totalCollections: Int, // 0
        @Json(name = "total_likes")
        val totalLikes: Int, // 23
        @Json(name = "total_photos")
        val totalPhotos: Int, // 1293
        @Json(name = "total_promoted_photos")
        val totalPromotedPhotos: Int, // 58
        @Json(name = "total_illustrations")
        val totalIllustrations: Int, // 0
        @Json(name = "total_promoted_illustrations")
        val totalPromotedIllustrations: Int, // 0
        @Json(name = "accepted_tos")
        val acceptedTos: Boolean, // true
        @Json(name = "for_hire")
        val forHire: Boolean, // false
        @Json(name = "social")
        val social: Social
    ) {
        @Keep
        data class Links(
            @Json(name = "self")
            val self: String, // https://api.unsplash.com/users/mbuff
            @Json(name = "html")
            val html: String, // https://unsplash.com/@mbuff
            @Json(name = "photos")
            val photos: String, // https://api.unsplash.com/users/mbuff/photos
            @Json(name = "likes")
            val likes: String, // https://api.unsplash.com/users/mbuff/likes
            @Json(name = "portfolio")
            val portfolio: String, // https://api.unsplash.com/users/mbuff/portfolio
            @Json(name = "following")
            val following: String, // https://api.unsplash.com/users/mbuff/following
            @Json(name = "followers")
            val followers: String // https://api.unsplash.com/users/mbuff/followers
        )

        @Keep
        data class ProfileImage(
            @Json(name = "small")
            val small: String, // https://images.unsplash.com/profile-1565090437718-b4fc7fc652f3?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
            @Json(name = "medium")
            val medium: String, // https://images.unsplash.com/profile-1565090437718-b4fc7fc652f3?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
            @Json(name = "large")
            val large: String // https://images.unsplash.com/profile-1565090437718-b4fc7fc652f3?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
        )

        @Keep
        data class Social(
            @Json(name = "instagram_username")
            val instagramUsername: String?, // ventiviews
            @Json(name = "portfolio_url")
            val portfolioUrl: String?, // http://ventiviews.com
            @Json(name = "twitter_username")
            val twitterUsername: String?, // Josh_Hild
            @Json(name = "paypal_email")
            val paypalEmail: Any? // null
        )
    }
}