package com.psd.learn.mysplash.worker

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.di.IoDispatcher
import com.psd.learn.mysplash.ui.utils.getCropHintRect
import com.psd.learn.mysplash.ui.utils.screenHeight
import com.psd.learn.mysplash.ui.utils.screenWidth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

@HiltWorker
class AutoSetWallpaperWorker @AssistedInject constructor(
    private val unSplashApiService: UnSplashApiService,
    private val photosLocalRepository: PhotosLocalRepository,
    private val unSplashPagingRepository: UnSplashPagingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result = withContext(dispatcher) {
        val collectionId = inputData.getString(REQUEST_COLLECTION_ID)
        val topicId = inputData.getString(REQUEST_TOPIC_ID)

        val resultRandomPhoto = unSplashPagingRepository.getRandomPhoto(collectionId, topicId)

        Log.d("sangpd", "doWork_randomPhoto: $resultRandomPhoto")
        if (resultRandomPhoto.isSuccess) {
            resultRandomPhoto.map { photoItem ->
                if (downloadAndSetWallpaper(photoItem)) {
                    photosLocalRepository.addWallpaperPhotoToHistory(photoItem.copy(isWallpaper = true))
                    return@withContext Result.success()
                } else {
                    return@withContext Result.retry()
                }
            }
        } else {
            return@withContext Result.retry()
        }
        return@withContext Result.success()
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
                return true
            }
        } catch (exception: Throwable) {
            Log.d("sangpd", "downloadAndSetWallpaper() - exception: $exception")
            return false
        }
    }

    companion object {
        private const val REQUEST_COLLECTION_ID = "request_collection_id"
        private const val REQUEST_TOPIC_ID = "request_topic_id"
        const val AUTO_REQUEST_INTERVAL = 15L
        private const val AUTO_WALLPAPER_WORKER_ID = "auto_set_wallpaper_worker_id"

        fun enqueueScheduleSetAutoWallpaper(
            context: Context,
            collectionId: String?,
            topicId: String?
        ) {
            val requestData = workDataOf(
                REQUEST_COLLECTION_ID to collectionId,
                REQUEST_TOPIC_ID to topicId
            )

            val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build()

            val request = PeriodicWorkRequestBuilder<AutoSetWallpaperWorker>(AUTO_REQUEST_INTERVAL, TimeUnit.MINUTES)
                .setInputData(requestData)
                .setConstraints(constraint)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                AUTO_WALLPAPER_WORKER_ID,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request
            )
            Toast.makeText(context, "Auto set wallpaper after $AUTO_REQUEST_INTERVAL minutes", Toast.LENGTH_SHORT).show()
        }

        fun cancelAutoWallpaperWorker(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(AUTO_WALLPAPER_WORKER_ID)
            Toast.makeText(context, "Auto set wallpaper was cancelled.", Toast.LENGTH_SHORT).show()
        }
    }
}