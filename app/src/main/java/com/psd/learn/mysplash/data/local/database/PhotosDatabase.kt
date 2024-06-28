package com.psd.learn.mysplash.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.psd.learn.mysplash.data.local.dao.PhotosDao
import com.psd.learn.mysplash.data.local.entity.MyConverter
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import kotlin.concurrent.Volatile

@Database(
    entities = [PhotoItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MyConverter::class)
abstract class PhotosDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotosDao

    companion object {
        private const val DB_NAME = "PHOTO_DATABASE.db"

        @Volatile
        private var INSTANCE: PhotosDatabase? = null

        private fun buildDatabase(context: Context): PhotosDatabase {
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = PhotosDatabase::class.java,
                name = DB_NAME
            ).build()
        }

        fun getInstance(context: Context): PhotosDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
    }
}