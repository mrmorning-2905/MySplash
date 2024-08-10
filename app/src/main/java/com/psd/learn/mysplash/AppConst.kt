package com.psd.learn.mysplash

import android.os.Environment

const val START_PAGE_INDEX = 1
const val PAGING_SIZE = 30
const val SEARCH_PHOTOS_TYPE = 0
const val SEARCH_COLLECTIONS_TYPE = 1
const val SEARCH_USERS_TYPE = 2
const val USER_DETAILS_PHOTOS_TYPE = 100
const val USER_DETAILS_COLLECTIONS_TYPE = 101
const val USER_DETAILS_LIKED_TYPE = 102

const val MY_SPLASH_DIRECTORY = "MySplash"
val MY_SPLASH_RELATIVE_PATH: String =
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path/*${File.separator}$MY_SPLASH_DIRECTORY"*/

object SortByType {
    const val LATEST_TYPE = "latest"
    const val OLDEST_TYPE = "oldest"
    const val POPULAR_TYPE = "popular"
    const val FEATURED_TYPE = "featured"
    const val POSITION_TYPE = "position"
}

const val PHOTO_SORT_BY_TYPE_KEY = "photo_sort_by_type_key"
const val TOPIC_SORT_BY_TYPE_KEY = "topic_sort_by_type_key"