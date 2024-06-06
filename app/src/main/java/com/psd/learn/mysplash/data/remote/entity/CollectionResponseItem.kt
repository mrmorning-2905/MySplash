package com.psd.learn.mysplash.data.remote.entity


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class CollectionResponseItem(
    @Json(name = "cover_photo")
    val coverPhoto: CoverPhoto,
    @Json(name = "description")
    val description: String?, // Discover the raw beauty of new motherhood with this collection.  Capturing tender moments and the profound bond between a mother and child.
    @Json(name = "featured")
    val featured: Boolean, // true
    @Json(name = "id")
    val id: String, // IzkerwNZhf8
    @Json(name = "last_collected_at")
    val lastCollectedAt: String, // 2024-05-10T18:57:53Z
    @Json(name = "links")
    val links: Links,
    @Json(name = "preview_photos")
    val previewPhotos: List<PreviewPhoto>,
    @Json(name = "private")
    val `private`: Boolean, // false
    @Json(name = "published_at")
    val publishedAt: String, // 2024-05-10T18:57:56Z
    @Json(name = "share_key")
    val shareKey: String, // 7e63302d603e6ee33501ebbd948a8d0b
    @Json(name = "title")
    val title: String, // New Moms
    @Json(name = "total_photos")
    val totalPhotos: Int, // 75
    @Json(name = "updated_at")
    val updatedAt: String, // 2024-05-10T18:57:56Z
    @Json(name = "user")
    val user: User
) {
    @Keep
    data class CoverPhoto(
        @Json(name = "alt_description")
        val altDescription: String? = null, // a pregnant woman holding a teddy bear in a crib
        @Json(name = "alternative_slugs")
        val alternativeSlugs: AlternativeSlugs,
        @Json(name = "asset_type")
        val assetType: String, // photo
        @Json(name = "blur_hash")
        val blurHash: String?, // LPO{,YR*lUayy?t8rWj?m-V@SvfP
        @Json(name = "breadcrumbs")
        val breadcrumbs: List<Breadcrumb>,
        @Json(name = "color")
        val color: String, // #EFEFEF
        @Json(name = "created_at")
        val createdAt: String, // 2022-10-03T18:53:50Z
        @Json(name = "current_user_collections")
        val currentUserCollections: List<Any>,
        @Json(name = "description")
        val description: String?, // Teardrops
        @Json(name = "height")
        val height: Int, // 4256
        @Json(name = "id")
        val id: String, // m8dpTopgEhw
        @Json(name = "liked_by_user")
        val likedByUser: Boolean, // false
        @Json(name = "likes")
        val likes: Int, // 7
        @Json(name = "links")
        val links: Links,
        @Json(name = "promoted_at")
        val promotedAt: String?, // 2024-04-11T08:45:06Z
        @Json(name = "slug")
        val slug: String, // a-pregnant-woman-holding-a-teddy-bear-in-a-crib-m8dpTopgEhw
        @Json(name = "sponsorship")
        val sponsorship: Any?, // null
        @Json(name = "updated_at")
        val updatedAt: String, // 2024-05-15T00:13:15Z
        @Json(name = "urls")
        val urls: Urls,
        @Json(name = "user")
        val user: User,
        @Json(name = "width")
        val width: Int // 2832
    ) {
        @Keep
        data class AlternativeSlugs(
            @Json(name = "de")
            val de: String, // eine-schwangere-frau-die-einen-teddybaren-in-einer-krippe-halt-m8dpTopgEhw
            @Json(name = "en")
            val en: String, // a-pregnant-woman-holding-a-teddy-bear-in-a-crib-m8dpTopgEhw
            @Json(name = "es")
            val es: String, // una-mujer-embarazada-sosteniendo-un-oso-de-peluche-en-una-cuna-m8dpTopgEhw
            @Json(name = "fr")
            val fr: String, // une-femme-enceinte-tenant-un-ours-en-peluche-dans-un-berceau-m8dpTopgEhw
            @Json(name = "it")
            val it: String, // una-donna-incinta-che-tiene-un-orsacchiotto-in-una-culla-m8dpTopgEhw
            @Json(name = "ja")
            val ja: String, // ベビーベッドにテディベアを持つ妊婦-m8dpTopgEhw
            @Json(name = "ko")
            val ko: String, // 아기-침대에서-곰-인형을-안고-있는-임산부-m8dpTopgEhw
            @Json(name = "pt")
            val pt: String // uma-mulher-gravida-segurando-um-ursinho-de-pelucia-em-um-berco-m8dpTopgEhw
        )

        @Keep
        data class Breadcrumb(
            @Json(name = "index")
            val index: Int, // 0
            @Json(name = "slug")
            val slug: String, // wallpapers
            @Json(name = "title")
            val title: String, // HD Wallpapers
            @Json(name = "type")
            val type: String // landing_page
        )

        @Keep
        data class Links(
            @Json(name = "download")
            val download: String, // https://unsplash.com/photos/m8dpTopgEhw/download
            @Json(name = "download_location")
            val downloadLocation: String, // https://api.unsplash.com/photos/m8dpTopgEhw/download
            @Json(name = "html")
            val html: String, // https://unsplash.com/photos/a-pregnant-woman-holding-a-teddy-bear-in-a-crib-m8dpTopgEhw
            @Json(name = "self")
            val self: String // https://api.unsplash.com/photos/a-pregnant-woman-holding-a-teddy-bear-in-a-crib-m8dpTopgEhw
        )

        @Keep
        data class Urls(
            @Json(name = "full")
            val full: String, // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb
            @Json(name = "raw")
            val raw: String, // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3
            @Json(name = "regular")
            val regular: String, // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max
            @Json(name = "small")
            val small: String, // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max
            @Json(name = "small_s3")
            val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/unsplash-premium-photos-production/premium_photo-1664453892209-05d289158feb
            @Json(name = "thumb")
            val thumb: String // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max
        )

        @Keep
        data class User(
            @Json(name = "accepted_tos")
            val acceptedTos: Boolean, // true
            @Json(name = "bio")
            val bio: String?, // I'm a Brazilian woman who loves to hear people’s thoughts, searching for projects that deeply impact my soul. Based in Rio, available to travel.
            @Json(name = "first_name")
            val firstName: String, // Natalia
            @Json(name = "for_hire")
            val forHire: Boolean, // true
            @Json(name = "id")
            val id: String, // rEM5C31SktQ
            @Json(name = "instagram_username")
            val instagramUsername: String?, // nataliablauth
            @Json(name = "last_name")
            val lastName: String? = null, // Blauth
            @Json(name = "links")
            val links: Links,
            @Json(name = "location")
            val location: String?, // Brasil
            @Json(name = "name")
            val name: String, // Natalia Blauth
            @Json(name = "portfolio_url")
            val portfolioUrl: String? = null, // http://nataliablauth.com/
            @Json(name = "profile_image")
            val profileImage: ProfileImage,
            @Json(name = "social")
            val social: Social,
            @Json(name = "total_collections")
            val totalCollections: Int, // 91
            @Json(name = "total_illustrations")
            val totalIllustrations: Int, // 0
            @Json(name = "total_likes")
            val totalLikes: Int, // 503
            @Json(name = "total_photos")
            val totalPhotos: Int, // 4705
            @Json(name = "total_promoted_illustrations")
            val totalPromotedIllustrations: Int, // 0
            @Json(name = "total_promoted_photos")
            val totalPromotedPhotos: Int, // 179
            @Json(name = "twitter_username")
            val twitterUsername: String?, // nataliablauth
            @Json(name = "updated_at")
            val updatedAt: String, // 2024-05-17T05:01:49Z
            @Json(name = "username")
            val username: String // nataliablauth
        ) {
            @Keep
            data class Links(
                @Json(name = "followers")
                val followers: String, // https://api.unsplash.com/users/nataliablauth/followers
                @Json(name = "following")
                val following: String, // https://api.unsplash.com/users/nataliablauth/following
                @Json(name = "html")
                val html: String, // https://unsplash.com/@nataliablauth
                @Json(name = "likes")
                val likes: String, // https://api.unsplash.com/users/nataliablauth/likes
                @Json(name = "photos")
                val photos: String, // https://api.unsplash.com/users/nataliablauth/photos
                @Json(name = "portfolio")
                val portfolio: String, // https://api.unsplash.com/users/nataliablauth/portfolio
                @Json(name = "self")
                val self: String // https://api.unsplash.com/users/nataliablauth
            )

            @Keep
            data class ProfileImage(
                @Json(name = "large")
                val large: String, // https://images.unsplash.com/profile-1699557651871-54656a2f2f63image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
                @Json(name = "medium")
                val medium: String, // https://images.unsplash.com/profile-1699557651871-54656a2f2f63image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
                @Json(name = "small")
                val small: String // https://images.unsplash.com/profile-1699557651871-54656a2f2f63image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
            )

            @Keep
            data class Social(
                @Json(name = "instagram_username")
                val instagramUsername: String?, // nataliablauth
                @Json(name = "portfolio_url")
                val portfolioUrl: String? = null, // http://nataliablauth.com/
                @Json(name = "twitter_username")
                val twitterUsername: String? // nataliablauth
            )
        }
    }

    @Keep
    data class Links(
        @Json(name = "html")
        val html: String, // https://unsplash.com/collections/IzkerwNZhf8/new-moms
        @Json(name = "photos")
        val photos: String, // https://api.unsplash.com/collections/IzkerwNZhf8/photos
        @Json(name = "related")
        val related: String, // https://api.unsplash.com/collections/IzkerwNZhf8/related
        @Json(name = "self")
        val self: String // https://api.unsplash.com/collections/IzkerwNZhf8
    )

    @Keep
    data class PreviewPhoto(
        @Json(name = "asset_type")
        val assetType: String, // photo
        @Json(name = "blur_hash")
        val blurHash: String?, // LMK1L1vJ-:H=?wM_RPs.%NaJMxk=
        @Json(name = "created_at")
        val createdAt: String, // 2022-10-03T18:53:50Z
        @Json(name = "id")
        val id: String, // m8dpTopgEhw
        @Json(name = "slug")
        val slug: String, // a-pregnant-woman-holding-a-teddy-bear-in-a-crib-m8dpTopgEhw
        @Json(name = "updated_at")
        val updatedAt: String, // 2024-05-15T00:13:15Z
        @Json(name = "urls")
        val urls: Urls
    ) {
        @Keep
        data class Urls(
            @Json(name = "full")
            val full: String, // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb
            @Json(name = "raw")
            val raw: String, // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3
            @Json(name = "regular")
            val regular: String, // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max
            @Json(name = "small")
            val small: String, // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max
            @Json(name = "small_s3")
            val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/unsplash-premium-photos-production/premium_photo-1664453892209-05d289158feb
            @Json(name = "thumb")
            val thumb: String // https://plus.unsplash.com/premium_photo-1664453892209-05d289158feb?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max
        )
    }

    @Keep
    data class User(
        @Json(name = "accepted_tos")
        val acceptedTos: Boolean, // true
        @Json(name = "bio")
        val bio: String?, // Creator + Head of Content at Pexels  |  Support me via the "PayPal" button ☝️  |  Donation Goal: $2 of $300 — Thank you Kathy R. 🖤
        @Json(name = "first_name")
        val firstName: String, // Unsplash+
        @Json(name = "for_hire")
        val forHire: Boolean, // false
        @Json(name = "id")
        val id: String, // iwi9-4OXLYY
        @Json(name = "instagram_username")
        val instagramUsername: String?, // andrewtneel
        @Json(name = "last_name")
        val lastName: String? = null, // Collections
        @Json(name = "links")
        val links: Links,
        @Json(name = "location")
        val location: String?, // North Carolina
        @Json(name = "name")
        val name: String, // Unsplash+ Collections
        @Json(name = "portfolio_url")
        val portfolioUrl: String? = null, // https://andrewtneel.com
        @Json(name = "profile_image")
        val profileImage: ProfileImage,
        @Json(name = "social")
        val social: Social,
        @Json(name = "total_collections")
        val totalCollections: Int, // 165
        @Json(name = "total_illustrations")
        val totalIllustrations: Int, // 0
        @Json(name = "total_likes")
        val totalLikes: Int, // 196
        @Json(name = "total_photos")
        val totalPhotos: Int, // 0
        @Json(name = "total_promoted_illustrations")
        val totalPromotedIllustrations: Int, // 0
        @Json(name = "total_promoted_photos")
        val totalPromotedPhotos: Int, // 0
        @Json(name = "twitter_username")
        val twitterUsername: String?, // andrewneel
        @Json(name = "updated_at")
        val updatedAt: String, // 2024-05-13T19:23:21Z
        @Json(name = "username")
        val username: String // unsplashplus
    ) {
        @Keep
        data class Links(
            @Json(name = "followers")
            val followers: String, // https://api.unsplash.com/users/unsplashplus/followers
            @Json(name = "following")
            val following: String, // https://api.unsplash.com/users/unsplashplus/following
            @Json(name = "html")
            val html: String, // https://unsplash.com/@unsplashplus
            @Json(name = "likes")
            val likes: String, // https://api.unsplash.com/users/unsplashplus/likes
            @Json(name = "photos")
            val photos: String, // https://api.unsplash.com/users/unsplashplus/photos
            @Json(name = "portfolio")
            val portfolio: String, // https://api.unsplash.com/users/unsplashplus/portfolio
            @Json(name = "self")
            val self: String // https://api.unsplash.com/users/unsplashplus
        )

        @Keep
        data class ProfileImage(
            @Json(name = "large")
            val large: String, // https://images.unsplash.com/profile-1714421769490-6918cb0c83a9image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
            @Json(name = "medium")
            val medium: String, // https://images.unsplash.com/profile-1714421769490-6918cb0c83a9image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
            @Json(name = "small")
            val small: String // https://images.unsplash.com/profile-1714421769490-6918cb0c83a9image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
        )

        @Keep
        data class Social(
            @Json(name = "instagram_username")
            val instagramUsername: String?, // andrewtneel
            @Json(name = "portfolio_url")
            val portfolioUrl: String? = null, // https://andrewtneel.com
            @Json(name = "twitter_username")
            val twitterUsername: String? // andrewneel
        )
    }
}