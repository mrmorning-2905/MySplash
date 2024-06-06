package com.psd.learn.mysplash.data.remote.entity


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class SearchCollectionResponseItem(
    @Json(name = "results")
    val results: List<Result>,
    @Json(name = "total")
    val total: Int, // 10000
    @Json(name = "total_pages")
    val totalPages: Int // 1000
) {
    @Keep
    data class Result(
        @Json(name = "cover_photo")
        val coverPhoto: CoverPhoto,
        @Json(name = "description")
        val description: Any?, // null
        @Json(name = "featured")
        val featured: Boolean, // false
        @Json(name = "id")
        val id: String, // 527632
        @Json(name = "last_collected_at")
        val lastCollectedAt: String, // 2024-05-06T18:39:09Z
        @Json(name = "links")
        val links: Links,
        @Json(name = "preview_photos")
        val previewPhotos: List<PreviewPhoto>,
        @Json(name = "private")
        val `private`: Boolean, // false
        @Json(name = "published_at")
        val publishedAt: String, // 2017-01-31T17:12:29Z
        @Json(name = "share_key")
        val shareKey: String, // bb9540d9f9135cabca590654a40ded7d
        @Json(name = "tags")
        val tags: List<Tag>,
        @Json(name = "title")
        val title: String, // Office
        @Json(name = "total_photos")
        val totalPhotos: Int, // 426
        @Json(name = "updated_at")
        val updatedAt: String, // 2024-05-06T18:39:09Z
        @Json(name = "user")
        val user: User
    ) {
        @Keep
        data class CoverPhoto(
            @Json(name = "alt_description")
            val altDescription: String? = null, // a desk with a computer on top of it
            @Json(name = "alternative_slugs")
            val alternativeSlugs: AlternativeSlugs,
            @Json(name = "asset_type")
            val assetType: String, // photo
            @Json(name = "blur_hash")
            val blurHash: String? = null, // L9IN,9^+XT9v1O%M?F%1MIIo9axt
            @Json(name = "breadcrumbs")
            val breadcrumbs: List<Breadcrumb>,
            @Json(name = "color")
            val color: String, // #8c7359
            @Json(name = "created_at")
            val createdAt: String, // 2023-10-17T08:09:24Z
            @Json(name = "current_user_collections")
            val currentUserCollections: List<Any>,
            @Json(name = "description")
            val description: String?, // colorful office
            @Json(name = "height")
            val height: Int, // 5201
            @Json(name = "id")
            val id: String, // 33PB9zr5gZo
            @Json(name = "liked_by_user")
            val likedByUser: Boolean, // false
            @Json(name = "likes")
            val likes: Int, // 77
            @Json(name = "links")
            val links: Links,
            @Json(name = "promoted_at")
            val promotedAt: String?, // 2023-10-27T05:00:02Z
            @Json(name = "slug")
            val slug: String, // a-desk-with-a-computer-on-top-of-it-33PB9zr5gZo
            @Json(name = "sponsorship")
            val sponsorship: Any?, // null
            @Json(name = "updated_at")
            val updatedAt: String, // 2024-05-17T13:15:47Z
            @Json(name = "urls")
            val urls: Urls,
            @Json(name = "user")
            val user: User,
            @Json(name = "width")
            val width: Int // 4081
        ) {
            @Keep
            data class AlternativeSlugs(
                @Json(name = "de")
                val de: String, // ein-schreibtisch-mit-einem-computer-darauf-33PB9zr5gZo
                @Json(name = "en")
                val en: String, // a-desk-with-a-computer-on-top-of-it-33PB9zr5gZo
                @Json(name = "es")
                val es: String, // un-escritorio-con-una-computadora-encima-33PB9zr5gZo
                @Json(name = "fr")
                val fr: String, // un-bureau-avec-un-ordinateur-dessus-33PB9zr5gZo
                @Json(name = "it")
                val it: String, // una-scrivania-con-un-computer-sopra-di-essa-33PB9zr5gZo
                @Json(name = "ja")
                val ja: String, // その上にコンピューターを置いた机-33PB9zr5gZo
                @Json(name = "ko")
                val ko: String, // 그-위에-컴퓨터가-있는-책상-33PB9zr5gZo
                @Json(name = "pt")
                val pt: String // uma-mesa-com-um-computador-em-cima-33PB9zr5gZo
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
                val download: String, // https://unsplash.com/photos/33PB9zr5gZo/download
                @Json(name = "download_location")
                val downloadLocation: String, // https://api.unsplash.com/photos/33PB9zr5gZo/download
                @Json(name = "html")
                val html: String, // https://unsplash.com/photos/a-desk-with-a-computer-on-top-of-it-33PB9zr5gZo
                @Json(name = "self")
                val self: String // https://api.unsplash.com/photos/a-desk-with-a-computer-on-top-of-it-33PB9zr5gZo
            )

            @Keep
            data class Urls(
                @Json(name = "full")
                val full: String, // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb
                @Json(name = "raw")
                val raw: String, // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3
                @Json(name = "regular")
                val regular: String, // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max
                @Json(name = "small")
                val small: String, // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max
                @Json(name = "small_s3")
                val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1697530152990-2c47d3cf7d7a
                @Json(name = "thumb")
                val thumb: String // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max
            )

            @Keep
            data class User(
                @Json(name = "accepted_tos")
                val acceptedTos: Boolean, // true
                @Json(name = "bio")
                val bio: String?, // Copenhagen-based photographer focusing on brand and lifestyle content. Visit my website for more info
                @Json(name = "first_name")
                val firstName: String, // Lasse
                @Json(name = "for_hire")
                val forHire: Boolean, // true
                @Json(name = "id")
                val id: String, // CU0kB_aF_X4
                @Json(name = "instagram_username")
                val instagramUsername: String? = null, // maybejensen
                @Json(name = "last_name")
                val lastName: String?, // Jensen
                @Json(name = "links")
                val links: Links,
                @Json(name = "location")
                val location: String?, // Denmark
                @Json(name = "name")
                val name: String, // Lasse Jensen
                @Json(name = "portfolio_url")
                val portfolioUrl: String?, // http://maybejensen.com
                @Json(name = "profile_image")
                val profileImage: ProfileImage,
                @Json(name = "social")
                val social: Social,
                @Json(name = "total_collections")
                val totalCollections: Int, // 6
                @Json(name = "total_illustrations")
                val totalIllustrations: Int, // 0
                @Json(name = "total_likes")
                val totalLikes: Int, // 317
                @Json(name = "total_photos")
                val totalPhotos: Int, // 158
                @Json(name = "total_promoted_illustrations")
                val totalPromotedIllustrations: Int, // 0
                @Json(name = "total_promoted_photos")
                val totalPromotedPhotos: Int, // 52
                @Json(name = "twitter_username")
                val twitterUsername: String?, // maybejensen
                @Json(name = "updated_at")
                val updatedAt: String, // 2024-04-24T01:24:34Z
                @Json(name = "username")
                val username: String // maybejensen
            ) {
                @Keep
                data class Links(
                    @Json(name = "followers")
                    val followers: String, // https://api.unsplash.com/users/maybejensen/followers
                    @Json(name = "following")
                    val following: String, // https://api.unsplash.com/users/maybejensen/following
                    @Json(name = "html")
                    val html: String, // https://unsplash.com/@maybejensen
                    @Json(name = "likes")
                    val likes: String, // https://api.unsplash.com/users/maybejensen/likes
                    @Json(name = "photos")
                    val photos: String, // https://api.unsplash.com/users/maybejensen/photos
                    @Json(name = "portfolio")
                    val portfolio: String, // https://api.unsplash.com/users/maybejensen/portfolio
                    @Json(name = "self")
                    val self: String // https://api.unsplash.com/users/maybejensen
                )

                @Keep
                data class ProfileImage(
                    @Json(name = "large")
                    val large: String, // https://images.unsplash.com/profile-1612347312199-765b00401715image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
                    @Json(name = "medium")
                    val medium: String, // https://images.unsplash.com/profile-1612347312199-765b00401715image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
                    @Json(name = "small")
                    val small: String // https://images.unsplash.com/profile-1612347312199-765b00401715image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
                )

                @Keep
                data class Social(
                    @Json(name = "instagram_username")
                    val instagramUsername: String? = null, // maybejensen
                    @Json(name = "paypal_email")
                    val paypalEmail: Any?, // null
                    @Json(name = "portfolio_url")
                    val portfolioUrl: String?, // http://maybejensen.com
                    @Json(name = "twitter_username")
                    val twitterUsername: String? // maybejensen
                )
            }
        }

        @Keep
        data class Links(
            @Json(name = "html")
            val html: String, // https://unsplash.com/collections/527632/office
            @Json(name = "photos")
            val photos: String, // https://api.unsplash.com/collections/527632/photos
            @Json(name = "related")
            val related: String, // https://api.unsplash.com/collections/527632/related
            @Json(name = "self")
            val self: String // https://api.unsplash.com/collections/527632
        )

        @Keep
        data class PreviewPhoto(
            @Json(name = "asset_type")
            val assetType: String, // photo
            @Json(name = "blur_hash")
            val blurHash: String? = null, // L9IN,9^+XT9v1O%M?F%1MIIo9axt
            @Json(name = "created_at")
            val createdAt: String, // 2023-10-17T08:09:24Z
            @Json(name = "id")
            val id: String, // 33PB9zr5gZo
            @Json(name = "slug")
            val slug: String, // a-desk-with-a-computer-on-top-of-it-33PB9zr5gZo
            @Json(name = "updated_at")
            val updatedAt: String, // 2024-05-17T13:15:47Z
            @Json(name = "urls")
            val urls: Urls
        ) {
            @Keep
            data class Urls(
                @Json(name = "full")
                val full: String, // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb
                @Json(name = "raw")
                val raw: String, // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3
                @Json(name = "regular")
                val regular: String, // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max
                @Json(name = "small")
                val small: String, // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max
                @Json(name = "small_s3")
                val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1697530152990-2c47d3cf7d7a
                @Json(name = "thumb")
                val thumb: String // https://images.unsplash.com/photo-1697530152990-2c47d3cf7d7a?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max
            )
        }

        @Keep
        data class Tag(
            @Json(name = "source")
            val source: Source?,
            @Json(name = "title")
            val title: String, // office
            @Json(name = "type")
            val type: String // search
        ) {
            @Keep
            data class Source(
                @Json(name = "ancestry")
                val ancestry: Ancestry,
                @Json(name = "cover_photo")
                val coverPhoto: CoverPhoto,
                @Json(name = "description")
                val description: String, // Looking for some beautiful images for your website? Unsplash has you covered. Find the perfect website background from our massive collection of professional-quality images. Each is free to use!
                @Json(name = "meta_description")
                val metaDescription: String, // Choose from hundreds of free website backgrounds. Download beautiful, curated free backgrounds on Unsplash.
                @Json(name = "meta_title")
                val metaTitle: String, // 900+ Website Background Images: Download HD Backgrounds on Unsplash
                @Json(name = "subtitle")
                val subtitle: String, // Download free website background images
                @Json(name = "title")
                val title: String // Website backgrounds
            ) {
                @Keep
                data class Ancestry(
                    @Json(name = "category")
                    val category: Category?,
                    @Json(name = "subcategory")
                    val subcategory: Subcategory?,
                    @Json(name = "type")
                    val type: Type
                ) {
                    @Keep
                    data class Category(
                        @Json(name = "pretty_slug")
                        val prettySlug: String, // App
                        @Json(name = "slug")
                        val slug: String // apps
                    )

                    @Keep
                    data class Subcategory(
                        @Json(name = "pretty_slug")
                        val prettySlug: String, // Website
                        @Json(name = "slug")
                        val slug: String // website
                    )

                    @Keep
                    data class Type(
                        @Json(name = "pretty_slug")
                        val prettySlug: String, // Backgrounds
                        @Json(name = "slug")
                        val slug: String // backgrounds
                    )
                }

                @Keep
                data class CoverPhoto(
                    @Json(name = "alt_description")
                    val altDescription: String? = null, // man in white crew neck t-shirt and black sunglasses standing on brown brick floor during
                    @Json(name = "alternative_slugs")
                    val alternativeSlugs: AlternativeSlugs,
                    @Json(name = "asset_type")
                    val assetType: String, // photo
                    @Json(name = "blur_hash")
                    val blurHash: String? = null, // LAHBYn00Dh9bMyRP4;s,Ql%1?coJ
                    @Json(name = "breadcrumbs")
                    val breadcrumbs: List<Breadcrumb>,
                    @Json(name = "color")
                    val color: String, // #c0a68c
                    @Json(name = "created_at")
                    val createdAt: String, // 2020-03-20T04:14:20Z
                    @Json(name = "current_user_collections")
                    val currentUserCollections: List<Any>,
                    @Json(name = "description")
                    val description: String?, // Summer in France with baby
                    @Json(name = "height")
                    val height: Int, // 4202
                    @Json(name = "id")
                    val id: String, // -zr2ZUMvChY
                    @Json(name = "liked_by_user")
                    val likedByUser: Boolean, // false
                    @Json(name = "likes")
                    val likes: Int, // 26
                    @Json(name = "links")
                    val links: Links,
                    @Json(name = "plus")
                    val plus: Boolean?, // false
                    @Json(name = "premium")
                    val premium: Boolean?, // false
                    @Json(name = "promoted_at")
                    val promotedAt: String?, // 2020-03-21T14:21:04Z
                    @Json(name = "slug")
                    val slug: String, // man-in-white-crew-neck-t-shirt-and-black-sunglasses-standing-on-brown-brick-floor-during--zr2ZUMvChY
                    @Json(name = "sponsorship")
                    val sponsorship: Any?, // null
                    @Json(name = "updated_at")
                    val updatedAt: String, // 2024-03-31T01:05:22Z
                    @Json(name = "urls")
                    val urls: Urls,
                    @Json(name = "user")
                    val user: User,
                    @Json(name = "width")
                    val width: Int // 3362
                ) {
                    @Keep
                    data class AlternativeSlugs(
                        @Json(name = "de")
                        val de: String, // mann-in-weissem-rundhals-t-shirt-und-schwarzer-sonnenbrille-auf-braunem-ziegelboden-wahrend--zr2ZUMvChY
                        @Json(name = "en")
                        val en: String, // man-in-white-crew-neck-t-shirt-and-black-sunglasses-standing-on-brown-brick-floor-during--zr2ZUMvChY
                        @Json(name = "es")
                        val es: String, // hombre-con-camiseta-blanca-de-cuello-redondo-y-gafas-de-sol-negras-de-pie-en-el-piso-de-ladrillo-marron-durante--zr2ZUMvChY
                        @Json(name = "fr")
                        val fr: String, // homme-en-t-shirt-a-col-rond-blanc-et-lunettes-de-soleil-noires-debout-sur-le-sol-en-briques-brunes-pendant--zr2ZUMvChY
                        @Json(name = "it")
                        val it: String, // uomo-in-t-shirt-girocollo-bianca-e-occhiali-da-sole-neri-in-piedi-sul-pavimento-di-mattoni-marroni-durante--zr2ZUMvChY
                        @Json(name = "ja")
                        val ja: String, // 茶色のレンガの床に立つ白いクルーネックのtシャツと黒いサングラスの男--zr2ZUMvChY
                        @Json(name = "ko")
                        val ko: String, // 흰색-크루넥-티셔츠를-입고-검은색-선글라스를-낀-남자가-갈색-벽돌-바닥에-서-있다--zr2ZUMvChY
                        @Json(name = "pt")
                        val pt: String // homem-em-camiseta-branca-de-pescoco-de-tripulacao-e-oculos-de-sol-pretos-em-pe-no-chao-de-tijolo-marrom-durante--zr2ZUMvChY
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
                        val download: String, // https://unsplash.com/photos/-zr2ZUMvChY/download
                        @Json(name = "download_location")
                        val downloadLocation: String, // https://api.unsplash.com/photos/-zr2ZUMvChY/download
                        @Json(name = "html")
                        val html: String, // https://unsplash.com/photos/man-in-white-crew-neck-t-shirt-and-black-sunglasses-standing-on-brown-brick-floor-during--zr2ZUMvChY
                        @Json(name = "self")
                        val self: String // https://api.unsplash.com/photos/man-in-white-crew-neck-t-shirt-and-black-sunglasses-standing-on-brown-brick-floor-during--zr2ZUMvChY
                    )

                    @Keep
                    data class Urls(
                        @Json(name = "full")
                        val full: String, // https://images.unsplash.com/photo-1584677626646-7c8f83690304?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb
                        @Json(name = "raw")
                        val raw: String, // https://images.unsplash.com/photo-1584677626646-7c8f83690304?ixlib=rb-4.0.3
                        @Json(name = "regular")
                        val regular: String, // https://images.unsplash.com/photo-1584677626646-7c8f83690304?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max
                        @Json(name = "small")
                        val small: String, // https://images.unsplash.com/photo-1584677626646-7c8f83690304?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max
                        @Json(name = "small_s3")
                        val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1584677626646-7c8f83690304
                        @Json(name = "thumb")
                        val thumb: String // https://images.unsplash.com/photo-1584677626646-7c8f83690304?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max
                    )

                    @Keep
                    data class User(
                        @Json(name = "accepted_tos")
                        val acceptedTos: Boolean, // true
                        @Json(name = "bio")
                        val bio: String? = null, // Hiperativo, Publicitario e amante da fotometria Instagram:@marcussantosr
                        @Json(name = "first_name")
                        val firstName: String, // Marcus
                        @Json(name = "for_hire")
                        val forHire: Boolean, // true
                        @Json(name = "id")
                        val id: String, // ba4iqw-n3No
                        @Json(name = "instagram_username")
                        val instagramUsername: String? = null, // marcussantosr
                        @Json(name = "last_name")
                        val lastName: String? = null, // Santos
                        @Json(name = "links")
                        val links: Links,
                        @Json(name = "location")
                        val location: String?, // Utah, United States
                        @Json(name = "name")
                        val name: String, // Marcus Santos
                        @Json(name = "portfolio_url")
                        val portfolioUrl: String?, // http://marcussantosr.com
                        @Json(name = "profile_image")
                        val profileImage: ProfileImage,
                        @Json(name = "social")
                        val social: Social,
                        @Json(name = "total_collections")
                        val totalCollections: Int, // 2
                        @Json(name = "total_illustrations")
                        val totalIllustrations: Int, // 0
                        @Json(name = "total_likes")
                        val totalLikes: Int, // 3
                        @Json(name = "total_photos")
                        val totalPhotos: Int, // 160
                        @Json(name = "total_promoted_illustrations")
                        val totalPromotedIllustrations: Int, // 0
                        @Json(name = "total_promoted_photos")
                        val totalPromotedPhotos: Int, // 73
                        @Json(name = "twitter_username")
                        val twitterUsername: String?, // thedakotacorbin
                        @Json(name = "updated_at")
                        val updatedAt: String, // 2024-04-05T02:33:01Z
                        @Json(name = "username")
                        val username: String // marcussantosr
                    ) {
                        @Keep
                        data class Links(
                            @Json(name = "followers")
                            val followers: String, // https://api.unsplash.com/users/marcussantosr/followers
                            @Json(name = "following")
                            val following: String, // https://api.unsplash.com/users/marcussantosr/following
                            @Json(name = "html")
                            val html: String, // https://unsplash.com/@marcussantosr
                            @Json(name = "likes")
                            val likes: String, // https://api.unsplash.com/users/marcussantosr/likes
                            @Json(name = "photos")
                            val photos: String, // https://api.unsplash.com/users/marcussantosr/photos
                            @Json(name = "portfolio")
                            val portfolio: String, // https://api.unsplash.com/users/marcussantosr/portfolio
                            @Json(name = "self")
                            val self: String // https://api.unsplash.com/users/marcussantosr
                        )

                        @Keep
                        data class ProfileImage(
                            @Json(name = "large")
                            val large: String, // https://images.unsplash.com/profile-fb-1545319920-8c2311af02a0.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
                            @Json(name = "medium")
                            val medium: String, // https://images.unsplash.com/profile-fb-1545319920-8c2311af02a0.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
                            @Json(name = "small")
                            val small: String // https://images.unsplash.com/profile-fb-1545319920-8c2311af02a0.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
                        )

                        @Keep
                        data class Social(
                            @Json(name = "instagram_username")
                            val instagramUsername: String? = null, // marcussantosr
                            @Json(name = "paypal_email")
                            val paypalEmail: Any?, // null
                            @Json(name = "portfolio_url")
                            val portfolioUrl: String?, // http://marcussantosr.com
                            @Json(name = "twitter_username")
                            val twitterUsername: String? // thedakotacorbin
                        )
                    }
                }
            }
        }

        @Keep
        data class User(
            @Json(name = "accepted_tos")
            val acceptedTos: Boolean, // false
            @Json(name = "bio")
            val bio: String?, // Curating the best of Unsplash.My Webflow Templates here_____________________________↓
            @Json(name = "first_name")
            val firstName: String, // Pablo
            @Json(name = "for_hire")
            val forHire: Boolean, // false
            @Json(name = "id")
            val id: String, // lAVYTPnGQ3k
            @Json(name = "instagram_username")
            val instagramUsername: String?, // hiyodesign
            @Json(name = "last_name")
            val lastName: String? = null, // Ramos
            @Json(name = "links")
            val links: Links,
            @Json(name = "location")
            val location: String?, // México
            @Json(name = "name")
            val name: String, // Pablo Ramos
            @Json(name = "portfolio_url")
            val portfolioUrl: String?, // https://webflow.com/templates/designers/pablo-ramos
            @Json(name = "profile_image")
            val profileImage: ProfileImage,
            @Json(name = "social")
            val social: Social,
            @Json(name = "total_collections")
            val totalCollections: Int, // 92
            @Json(name = "total_illustrations")
            val totalIllustrations: Int, // 0
            @Json(name = "total_likes")
            val totalLikes: Int, // 9
            @Json(name = "total_photos")
            val totalPhotos: Int, // 0
            @Json(name = "total_promoted_illustrations")
            val totalPromotedIllustrations: Int, // 0
            @Json(name = "total_promoted_photos")
            val totalPromotedPhotos: Int, // 0
            @Json(name = "twitter_username")
            val twitterUsername: String?, // pabloravel
            @Json(name = "updated_at")
            val updatedAt: String, // 2024-05-07T09:27:32Z
            @Json(name = "username")
            val username: String // pabloravel
        ) {
            @Keep
            data class Links(
                @Json(name = "followers")
                val followers: String, // https://api.unsplash.com/users/pabloravel/followers
                @Json(name = "following")
                val following: String, // https://api.unsplash.com/users/pabloravel/following
                @Json(name = "html")
                val html: String, // https://unsplash.com/@pabloravel
                @Json(name = "likes")
                val likes: String, // https://api.unsplash.com/users/pabloravel/likes
                @Json(name = "photos")
                val photos: String, // https://api.unsplash.com/users/pabloravel/photos
                @Json(name = "portfolio")
                val portfolio: String, // https://api.unsplash.com/users/pabloravel/portfolio
                @Json(name = "self")
                val self: String // https://api.unsplash.com/users/pabloravel
            )

            @Keep
            data class ProfileImage(
                @Json(name = "large")
                val large: String, // https://images.unsplash.com/profile-1489618200917-0dea409e236f?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128
                @Json(name = "medium")
                val medium: String, // https://images.unsplash.com/profile-1489618200917-0dea409e236f?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64
                @Json(name = "small")
                val small: String // https://images.unsplash.com/profile-1489618200917-0dea409e236f?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32
            )

            @Keep
            data class Social(
                @Json(name = "instagram_username")
                val instagramUsername: String?, // hiyodesign
                @Json(name = "paypal_email")
                val paypalEmail: Any?, // null
                @Json(name = "portfolio_url")
                val portfolioUrl: String?, // https://webflow.com/templates/designers/pablo-ramos
                @Json(name = "twitter_username")
                val twitterUsername: String? // pabloravel
            )
        }
    }
}