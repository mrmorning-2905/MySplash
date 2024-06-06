package com.psd.learn.mysplash.data.remote.entity


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class SearchPhotoResponseItem(
    @Json(name = "results")
    val results: List<Result>,
    @Json(name = "total")
    val total: Int, // 10000
    @Json(name = "total_pages")
    val totalPages: Int // 1000
) {
    @Keep
    data class Result(
        @Json(name = "alt_description")
        val altDescription: String, // turned off laptop computer on top of brown wooden table
        @Json(name = "alternative_slugs")
        val alternativeSlugs: AlternativeSlugs,
        @Json(name = "asset_type")
        val assetType: String, // photo
        @Json(name = "blur_hash")
        val blurHash: String? = null, // LTKeG.00Rj%3%ND%oLxuWAaxaeof
        @Json(name = "breadcrumbs")
        val breadcrumbs: List<Breadcrumb>,
        @Json(name = "color")
        val color: String, // #d9d9d9
        @Json(name = "created_at")
        val createdAt: String, // 2017-06-11T21:17:03Z
        @Json(name = "current_user_collections")
        val currentUserCollections: List<Any>,
        @Json(name = "description")
        val description: String?, // Office
        @Json(name = "height")
        val height: Int, // 3648
        @Json(name = "id")
        val id: String, // VWcPlbHglYc
        @Json(name = "liked_by_user")
        val likedByUser: Boolean, // false
        @Json(name = "likes")
        val likes: Int, // 3378
        @Json(name = "links")
        val links: Links,
        @Json(name = "promoted_at")
        val promotedAt: String?, // 2017-06-12T10:24:27Z
        @Json(name = "slug")
        val slug: String, // turned-off-laptop-computer-on-top-of-brown-wooden-table-VWcPlbHglYc
        @Json(name = "sponsorship")
        val sponsorship: Any?, // null
        @Json(name = "tags")
        val tags: List<Tag>,
        @Json(name = "updated_at")
        val updatedAt: String, // 2024-05-15T19:34:15Z
        @Json(name = "urls")
        val urls: Urls,
        @Json(name = "user")
        val user: User,
        @Json(name = "width")
        val width: Int // 5472
    ) {
        @Keep
        data class AlternativeSlugs(
            @Json(name = "de")
            val de: String, // ausgeschalteter-laptop-auf-braunem-holztisch-VWcPlbHglYc
            @Json(name = "en")
            val en: String, // turned-off-laptop-computer-on-top-of-brown-wooden-table-VWcPlbHglYc
            @Json(name = "es")
            val es: String, // computadora-portatil-apagada-encima-de-una-mesa-de-madera-marron-VWcPlbHglYc
            @Json(name = "fr")
            val fr: String, // ordinateur-portable-eteint-sur-une-table-en-bois-brun-VWcPlbHglYc
            @Json(name = "it")
            val it: String, // computer-portatile-spento-sopra-il-tavolo-di-legno-marrone-VWcPlbHglYc
            @Json(name = "ja")
            val ja: String, // 茶色の木製のテーブルの上でラップトップコンピュータの電源を切った-VWcPlbHglYc
            @Json(name = "ko")
            val ko: String, // 갈색-나무-테이블-위에-노트북-컴퓨터를-껐습니다-VWcPlbHglYc
            @Json(name = "pt")
            val pt: String // computador-portatil-desligado-em-cima-da-mesa-de-madeira-marrom-VWcPlbHglYc
        )

        @Keep
        data class Breadcrumb(
            @Json(name = "index")
            val index: Int, // 0
            @Json(name = "slug")
            val slug: String, // backgrounds
            @Json(name = "title")
            val title: String, // HQ Background Images
            @Json(name = "type")
            val type: String // landing_page
        )

        @Keep
        data class Links(
            @Json(name = "download")
            val download: String, // https://unsplash.com/photos/VWcPlbHglYc/download?ixid=M3w2MDE2MDl8MHwxfHNlYXJjaHwxfHxvZmZpY2V8ZW58MHx8fHwxNzE2MDAwNDM1fDA
            @Json(name = "download_location")
            val downloadLocation: String, // https://api.unsplash.com/photos/VWcPlbHglYc/download?ixid=M3w2MDE2MDl8MHwxfHNlYXJjaHwxfHxvZmZpY2V8ZW58MHx8fHwxNzE2MDAwNDM1fDA
            @Json(name = "html")
            val html: String, // https://unsplash.com/photos/turned-off-laptop-computer-on-top-of-brown-wooden-table-VWcPlbHglYc
            @Json(name = "self")
            val self: String // https://api.unsplash.com/photos/turned-off-laptop-computer-on-top-of-brown-wooden-table-VWcPlbHglYc
        )

        @Keep
        data class Tag(
            @Json(name = "source")
            val source: Source?,
            @Json(name = "title")
            val title: String, // laptop
            @Json(name = "type")
            val type: String // landing_page
        ) {
            @Keep
            data class Source(
                @Json(name = "cover_photo")
                val coverPhoto: CoverPhoto,
                @Json(name = "description")
                val description: String, // Choose from a curated selection of laptop wallpapers for your mobile and desktop screens. Always free on Unsplash.
                @Json(name = "meta_description")
                val metaDescription: String, // Choose from hundreds of free laptop wallpapers. Download HD wallpapers for free on Unsplash.
                @Json(name = "meta_title")
                val metaTitle: String, // Laptop Wallpapers: Free HD Download [500+ HQ] | Unsplash
                @Json(name = "subtitle")
                val subtitle: String, // Download free laptop wallpapers
                @Json(name = "title")
                val title: String // Hd laptop wallpapers
            ) {
                @Keep
                data class CoverPhoto(
                    @Json(name = "alt_description")
                    val altDescription: String, // red fox standing on grass field
                    @Json(name = "alternative_slugs")
                    val alternativeSlugs: AlternativeSlugs,
                    @Json(name = "asset_type")
                    val assetType: String, // photo
                    @Json(name = "blur_hash")
                    val blurHash: String? = null, // LAD,i29I0O.M^%IWjK-o4:RkxXr_
                    @Json(name = "breadcrumbs")
                    val breadcrumbs: List<Breadcrumb>,
                    @Json(name = "color")
                    val color: String, // #597340
                    @Json(name = "created_at")
                    val createdAt: String, // 2017-07-20T06:16:22Z
                    @Json(name = "current_user_collections")
                    val currentUserCollections: List<Any>,
                    @Json(name = "description")
                    val description: String?, // Red Fox in a Field
                    @Json(name = "height")
                    val height: Int, // 3425
                    @Json(name = "id")
                    val id: String, // 7TGVEgcTKlY
                    @Json(name = "liked_by_user")
                    val likedByUser: Boolean, // false
                    @Json(name = "likes")
                    val likes: Int, // 1710
                    @Json(name = "links")
                    val links: Links,
                    @Json(name = "plus")
                    val plus: Boolean?, // false
                    @Json(name = "premium")
                    val premium: Boolean?, // false
                    @Json(name = "promoted_at")
                    val promotedAt: String?, // 2017-07-20T15:42:14Z
                    @Json(name = "slug")
                    val slug: String, // red-fox-standing-on-grass-field-7TGVEgcTKlY
                    @Json(name = "sponsorship")
                    val sponsorship: Any?, // null
                    @Json(name = "updated_at")
                    val updatedAt: String, // 2024-05-10T19:17:44Z
                    @Json(name = "urls")
                    val urls: Urls,
                    @Json(name = "user")
                    val user: User,
                    @Json(name = "width")
                    val width: Int // 5130
                ) {
                    @Keep
                    data class AlternativeSlugs(
                        @Json(name = "de")
                        val de: String, // rotfuchs-steht-auf-rasen-7TGVEgcTKlY
                        @Json(name = "en")
                        val en: String, // red-fox-standing-on-grass-field-7TGVEgcTKlY
                        @Json(name = "es")
                        val es: String, // zorro-rojo-de-pie-en-el-campo-de-hierba-7TGVEgcTKlY
                        @Json(name = "fr")
                        val fr: String, // renard-roux-debout-sur-le-champ-dherbe-7TGVEgcTKlY
                        @Json(name = "it")
                        val it: String, // volpe-rossa-in-piedi-sul-campo-derba-7TGVEgcTKlY
                        @Json(name = "ja")
                        val ja: String, // 草原に立つアカギツネ-7TGVEgcTKlY
                        @Json(name = "ko")
                        val ko: String, // 잔디밭에-서-있는-붉은-여우-7TGVEgcTKlY
                        @Json(name = "pt")
                        val pt: String // raposa-vermelha-em-pe-no-campo-de-grama-7TGVEgcTKlY
                    )

                    @Keep
                    data class Breadcrumb(
                        @Json(name = "index")
                        val index: Int, // 0
                        @Json(name = "slug")
                        val slug: String, // images
                        @Json(name = "title")
                        val title: String, // 1,000,000+ Free Images
                        @Json(name = "type")
                        val type: String // landing_page
                    )

                    @Keep
                    data class Links(
                        @Json(name = "download")
                        val download: String, // https://unsplash.com/photos/7TGVEgcTKlY/download
                        @Json(name = "download_location")
                        val downloadLocation: String, // https://api.unsplash.com/photos/7TGVEgcTKlY/download
                        @Json(name = "html")
                        val html: String, // https://unsplash.com/photos/red-fox-standing-on-grass-field-7TGVEgcTKlY
                        @Json(name = "self")
                        val self: String // https://api.unsplash.com/photos/red-fox-standing-on-grass-field-7TGVEgcTKlY
                    )

                    @Keep
                    data class Urls(
                        @Json(name = "full")
                        val full: String, // https://images.unsplash.com/photo-1500531359996-c89a0e63e49c?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb
                        @Json(name = "raw")
                        val raw: String, // https://images.unsplash.com/photo-1500531359996-c89a0e63e49c?ixlib=rb-4.0.3
                        @Json(name = "regular")
                        val regular: String, // https://images.unsplash.com/photo-1500531359996-c89a0e63e49c?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max
                        @Json(name = "small")
                        val small: String, // https://images.unsplash.com/photo-1500531359996-c89a0e63e49c?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max
                        @Json(name = "small_s3")
                        val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1500531359996-c89a0e63e49c
                        @Json(name = "thumb")
                        val thumb: String // https://images.unsplash.com/photo-1500531359996-c89a0e63e49c?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max
                    )

                    @Keep
                    data class User(
                        @Json(name = "accepted_tos")
                        val acceptedTos: Boolean, // true
                        @Json(name = "bio")
                        val bio: String? = null, // Prints now available at Kunack.us
                        @Json(name = "first_name")
                        val firstName: String, // Nathan
                        @Json(name = "for_hire")
                        val forHire: Boolean, // true
                        @Json(name = "id")
                        val id: String, // UT41ainZxFI
                        @Json(name = "instagram_username")
                        val instagramUsername: String? = null, // kunack.us
                        @Json(name = "last_name")
                        val lastName: String? = null, // Anderson
                        @Json(name = "links")
                        val links: Links,
                        @Json(name = "location")
                        val location: String? = null, // Knoxville, TN
                        @Json(name = "name")
                        val name: String, // Nathan Anderson
                        @Json(name = "portfolio_url")
                        val portfolioUrl: String?, // https://kunack.us/follow?source=unsplash
                        @Json(name = "profile_image")
                        val profileImage: ProfileImage,
                        @Json(name = "social")
                        val social: Social,
                        @Json(name = "total_collections")
                        val totalCollections: Int, // 9
                        @Json(name = "total_illustrations")
                        val totalIllustrations: Int, // 0
                        @Json(name = "total_likes")
                        val totalLikes: Int, // 1224
                        @Json(name = "total_photos")
                        val totalPhotos: Int, // 1344
                        @Json(name = "total_promoted_illustrations")
                        val totalPromotedIllustrations: Int, // 0
                        @Json(name = "total_promoted_photos")
                        val totalPromotedPhotos: Int, // 433
                        @Json(name = "twitter_username")
                        val twitterUsername: String?, // nathanworking
                        @Json(name = "updated_at")
                        val updatedAt: String, // 2024-05-12T17:56:57Z
                        @Json(name = "username")
                        val username: String // nathananderson
                    ) {
                        @Keep
                        data class Links(
                            @Json(name = "followers")
                            val followers: String, // https://api.unsplash.com/users/nathananderson/followers
                            @Json(name = "following")
                            val following: String, // https://api.unsplash.com/users/nathananderson/following
                            @Json(name = "html")
                            val html: String, // https://unsplash.com/@nathananderson
                            @Json(name = "likes")
                            val likes: String, // https://api.unsplash.com/users/nathananderson/likes
                            @Json(name = "photos")
                            val photos: String, // https://api.unsplash.com/users/nathananderson/photos
                            @Json(name = "portfolio")
                            val portfolio: String, // https://api.unsplash.com/users/nathananderson/portfolio
                            @Json(name = "self")
                            val self: String // https://api.unsplash.com/users/nathananderson
                        )

                        @Keep
                        data class ProfileImage(
                            @Json(name = "large")
                            val large: String, // https://images.unsplash.com/profile-1676320214445-e3fec6f7b87cimage?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
                            @Json(name = "medium")
                            val medium: String, // https://images.unsplash.com/profile-1676320214445-e3fec6f7b87cimage?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
                            @Json(name = "small")
                            val small: String // https://images.unsplash.com/profile-1676320214445-e3fec6f7b87cimage?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
                        )

                        @Keep
                        data class Social(
                            @Json(name = "instagram_username")
                            val instagramUsername: String? = null, // kunack.us
                            @Json(name = "paypal_email")
                            val paypalEmail: Any?, // null
                            @Json(name = "portfolio_url")
                            val portfolioUrl: String?, // https://kunack.us/follow?source=unsplash
                            @Json(name = "twitter_username")
                            val twitterUsername: String? // nathanworking
                        )
                    }
                }
            }
        }

        @Keep
        data class Urls(
            @Json(name = "full")
            val full: String, // https://images.unsplash.com/photo-1497215728101-856f4ea42174?crop=entropy&cs=srgb&fm=jpg&ixid=M3w2MDE2MDl8MHwxfHNlYXJjaHwxfHxvZmZpY2V8ZW58MHx8fHwxNzE2MDAwNDM1fDA&ixlib=rb-4.0.3&q=85
            @Json(name = "raw")
            val raw: String, // https://images.unsplash.com/photo-1497215728101-856f4ea42174?ixid=M3w2MDE2MDl8MHwxfHNlYXJjaHwxfHxvZmZpY2V8ZW58MHx8fHwxNzE2MDAwNDM1fDA&ixlib=rb-4.0.3
            @Json(name = "regular")
            val regular: String, // https://images.unsplash.com/photo-1497215728101-856f4ea42174?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MDE2MDl8MHwxfHNlYXJjaHwxfHxvZmZpY2V8ZW58MHx8fHwxNzE2MDAwNDM1fDA&ixlib=rb-4.0.3&q=80&w=1080
            @Json(name = "small")
            val small: String, // https://images.unsplash.com/photo-1497215728101-856f4ea42174?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MDE2MDl8MHwxfHNlYXJjaHwxfHxvZmZpY2V8ZW58MHx8fHwxNzE2MDAwNDM1fDA&ixlib=rb-4.0.3&q=80&w=400
            @Json(name = "small_s3")
            val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1497215728101-856f4ea42174
            @Json(name = "thumb")
            val thumb: String // https://images.unsplash.com/photo-1497215728101-856f4ea42174?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MDE2MDl8MHwxfHNlYXJjaHwxfHxvZmZpY2V8ZW58MHx8fHwxNzE2MDAwNDM1fDA&ixlib=rb-4.0.3&q=80&w=200
        )

        @Keep
        data class User(
            @Json(name = "accepted_tos")
            val acceptedTos: Boolean, // true
            @Json(name = "bio")
            val bio: String?= null, // Ottawa-based photographer who loves adventures. Gear: Canon 6D; Leica Q2
            @Json(name = "first_name")
            val firstName: String, // Alesia
            @Json(name = "for_hire")
            val forHire: Boolean, // true
            @Json(name = "id")
            val id: String, // zJgeEcvxc0o
            @Json(name = "instagram_username")
            val instagramUsername: String?, // saltnstreets
            @Json(name = "last_name")
            val lastName: String?, // Kazantceva
            @Json(name = "links")
            val links: Links,
            @Json(name = "location")
            val location: String? = null, // Ottawa
            @Json(name = "name")
            val name: String, // Alesia Kazantceva
            @Json(name = "portfolio_url")
            val portfolioUrl: String?, // http://saltnstreets.com
            @Json(name = "profile_image")
            val profileImage: ProfileImage,
            @Json(name = "social")
            val social: Social,
            @Json(name = "total_collections")
            val totalCollections: Int, // 9
            @Json(name = "total_illustrations")
            val totalIllustrations: Int, // 0
            @Json(name = "total_likes")
            val totalLikes: Int, // 43
            @Json(name = "total_photos")
            val totalPhotos: Int, // 60
            @Json(name = "total_promoted_illustrations")
            val totalPromotedIllustrations: Int, // 0
            @Json(name = "total_promoted_photos")
            val totalPromotedPhotos: Int, // 16
            @Json(name = "twitter_username")
            val twitterUsername: String?, // toaheftiba
            @Json(name = "updated_at")
            val updatedAt: String, // 2024-05-17T20:55:33Z
            @Json(name = "username")
            val username: String // alesiaskaz
        ) {
            @Keep
            data class Links(
                @Json(name = "followers")
                val followers: String, // https://api.unsplash.com/users/alesiaskaz/followers
                @Json(name = "following")
                val following: String, // https://api.unsplash.com/users/alesiaskaz/following
                @Json(name = "html")
                val html: String, // https://unsplash.com/@alesiaskaz
                @Json(name = "likes")
                val likes: String, // https://api.unsplash.com/users/alesiaskaz/likes
                @Json(name = "photos")
                val photos: String, // https://api.unsplash.com/users/alesiaskaz/photos
                @Json(name = "portfolio")
                val portfolio: String, // https://api.unsplash.com/users/alesiaskaz/portfolio
                @Json(name = "self")
                val self: String // https://api.unsplash.com/users/alesiaskaz
            )

            @Keep
            data class ProfileImage(
                @Json(name = "large")
                val large: String, // https://images.unsplash.com/profile-1596578750003-cc29df35976eimage?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
                @Json(name = "medium")
                val medium: String, // https://images.unsplash.com/profile-1596578750003-cc29df35976eimage?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
                @Json(name = "small")
                val small: String // https://images.unsplash.com/profile-1596578750003-cc29df35976eimage?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
            )

            @Keep
            data class Social(
                @Json(name = "instagram_username")
                val instagramUsername: String?, // saltnstreets
                @Json(name = "paypal_email")
                val paypalEmail: Any?, // null
                @Json(name = "portfolio_url")
                val portfolioUrl: String?, // http://saltnstreets.com
                @Json(name = "twitter_username")
                val twitterUsername: String? // toaheftiba
            )
        }
    }
}