package com.psd.learn.mysplash.worker

import android.app.WallpaperManager
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.di.IoDispatcher
import com.psd.learn.mysplash.ui.utils.getCropHintRect
import com.psd.learn.mysplash.ui.utils.screenHeight
import com.psd.learn.mysplash.ui.utils.screenWidth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

@HiltWorker
class SingleSetWallpaperWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val unSplashApiService: UnSplashApiService,
    private val photosLocalRepository: PhotosLocalRepository,
    private val gson: Gson
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcher) {
        val jsonData = inputData.getString(PHOTO_INFO_REQUEST)
        Log.d("sangpd", "doWork_jsonData: $jsonData")
        val photoItem = gson.fromJson(jsonData, PhotoItem::class.java) ?: return@withContext Result.failure()
        if (downloadAndSetWallpaper(photoItem)) {
            photosLocalRepository.addWallpaperPhotoToHistory(photoItem.copy(isWallpaper = true))
            Log.d("sangpd", "doWork: success")
            return@withContext Result.success()
        } else {
            Log.d("sangpd", "doWork: failed")
            return@withContext Result.failure()
        }
    }

    private suspend fun downloadAndSetWallpaper(photo: PhotoItem): Boolean {
        try {
            val responseBody = unSplashApiService.openUrl(photo.coverPhotoUrl)
            responseBody.byteStream().use { inputStream ->
                val centerCropRect = getCropHintRect(
                    min(screenWidth, screenHeight).toDouble(),
                    max(screenWidth, screenHeight).toDouble(),
                    photo.width.toDouble(),
                    photo.height.toDouble()
                )
                val selectScreen = WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                WallpaperManager.getInstance(context).setStream(inputStream, centerCropRect, true, selectScreen)
                Log.d("sangpd", "downloadAndSetWallpaper_done")
                return true
            }
        } catch (exception: Throwable) {
            Log.d("sangpd", "downloadAndSetWallpaper() - exception: $exception")
            return false
        }
    }

    companion object {

        const val SINGLE_WALLPAPER_WORKER_ID = "single_set_wallpaper_worker_id"
        private const val PHOTO_INFO_REQUEST = "photo_info_request"

        fun enqueueSingleSetWallPaperWork(context: Context, gson: Gson, photo: PhotoItem) {
            Log.d("sangpd", "enqueueSingleSetWallPaperWork_photo: ${photo.photoId}")
            val requestInfo = gson.toJson(photo)
            val requestData = workDataOf(PHOTO_INFO_REQUEST to requestInfo)
            val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build()

            val request = OneTimeWorkRequestBuilder<SingleSetWallpaperWorker>()
                .setInputData(requestData)
                .setConstraints(constraint)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                SINGLE_WALLPAPER_WORKER_ID,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }
}