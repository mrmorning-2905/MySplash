package com.psd.learn.mysplash.data.remote.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class PhotoResponseItem(
    @Json(name = "id")
    val id: String, // Ga7aBzN7qDw
    @Json(name = "created_at")
    val createdAt: String, // 2021-01-11T19:03:43Z
    @Json(name = "updated_at")
    val updatedAt: String, // 2024-05-25T10:22:02Z
    @Json(name = "width")
    val width: Int?, // 3600
    @Json(name = "height")
    val height: Int?, // 4199
    @Json(name = "color")
    val color: String?,
    @Json(name = "blur_hash")
    val blurHash: String?, // LPG]7-xtrzt603afofay=%WESuay
    @Json(name = "description")
    val description: String?, // A minimalistic image of a single white anemone (wild swan) flower, with a yellow centre, in a white vase, set against a yellow background.
    @Json(name = "alt_description")
    val altDescription: String?, // green wooden door on gray concrete floor
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "links")
    val links: Links?,
    @Json(name = "likes")
    val likes: Int?, // 59
    @Json(name = "liked_by_user")
    val likedByUser: Boolean?, // false
    @Json(name = "user")
    val userResponse: UserResponseItem?,
    @Json(name = "exif")
    val exif: Exif?,
    @Json(name = "location")
    val location: Location?,
    @Json(name = "tags")
    val tags: List<Tag>?,
    @Json(name = "views")
    val views: Int?, // 223
    @Json(name = "downloads")
    val downloads: Int?, // 3
) {
    @Keep
    data class Urls(
        @Json(name = "raw")
        val raw: String, // https://images.unsplash.com/photo-1610391547035-b4541ce32da0?ixid=M3w2MTQ2Njh8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNjYzNTQ0MHw&ixlib=rb-4.0.3
        @Json(name = "full")
        val full: String, // https://images.unsplash.com/photo-1610391547035-b4541ce32da0?crop=entropy&cs=srgb&fm=jpg&ixid=M3w2MTQ2Njh8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNjYzNTQ0MHw&ixlib=rb-4.0.3&q=85
        @Json(name = "regular")
        val regular: String, // https://images.unsplash.com/photo-1610391547035-b4541ce32da0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTQ2Njh8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNjYzNTQ0MHw&ixlib=rb-4.0.3&q=80&w=1080
        @Json(name = "small")
        val small: String, // https://images.unsplash.com/photo-1610391547035-b4541ce32da0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTQ2Njh8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNjYzNTQ0MHw&ixlib=rb-4.0.3&q=80&w=400
        @Json(name = "thumb")
        val thumb: String, // https://images.unsplash.com/photo-1610391547035-b4541ce32da0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTQ2Njh8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNjYzNTQ0MHw&ixlib=rb-4.0.3&q=80&w=200
        @Json(name = "small_s3")
        val smallS3: String // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1610391547035-b4541ce32da0
    )

    @Keep
    data class Links(
        @Json(name = "self")
        val self: String, // https://api.unsplash.com/photos/green-wooden-door-on-gray-concrete-floor-Ga7aBzN7qDw
        @Json(name = "html")
        val html: String, // https://unsplash.com/photos/green-wooden-door-on-gray-concrete-floor-Ga7aBzN7qDw
        @Json(name = "download")
        val download: String, // https://unsplash.com/photos/Ga7aBzN7qDw/download?ixid=M3w2MTQ2Njh8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNjYzNTQ0MHw
        @Json(name = "download_location")
        val downloadLocation: String // https://api.unsplash.com/photos/Ga7aBzN7qDw/download?ixid=M3w2MTQ2Njh8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNjYzNTQ0MHw
    )

    @Keep
    data class Exif(
        @Json(name = "make")
        val make: String?, // SONY
        @Json(name = "model")
        val model: String?, // ILCE-7M3
        @Json(name = "name")
        val name: String?, // SONY, ILCE-7M3
        @Json(name = "exposure_time")
        val exposureTime: String?, // 1/125
        @Json(name = "aperture")
        val aperture: String?, // 2.8
        @Json(name = "focal_length")
        val focalLength: String?, // 28.0
        @Json(name = "iso")
        val iso: Int? // 100
    )

    @Keep
    data class Location(
        @Json(name = "name")
        val name: String?, // Tokyo Shinbashi, Japan
        @Json(name = "city")
        val city: String?, // null
        @Json(name = "country")
        val country: String?, // null
        @Json(name = "position")
        val position: Position
    )

    @Keep
    data class Position(
        @Json(name = "latitude")
        val latitude: Double?, // 0.0
        @Json(name = "longitude")
        val longitude: Double? // 0.0
    )

    @Keep
    data class Tag(
        @Json(name = "type")
        val type: String, // search
        @Json(name = "title")
        val title: String, // japan
    )
}