package com.psd.learn.mysplash.data.local.entity

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun buildMoshi(): Moshi {
    return Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
}

class MyConverter {

    private val moshi by lazy { buildMoshi() }
    private val moshiAdapter = moshi.adapter<Set<String>>(Set::class.java)

    @TypeConverter
    fun fromJsonToListTag(json: String): Set<String> {
        return moshiAdapter.fromJson(json).orEmpty()
    }

    @TypeConverter
    fun listTagToJson(tags: Set<String>): String {
        return moshiAdapter.toJson(tags)
    }
}