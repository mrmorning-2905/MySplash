package com.psd.learn.mysplash.worker

import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.pm.ServiceInfo
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.psd.learn.mysplash.MY_SPLASH_RELATIVE_PATH
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.di.IoDispatcher
import com.psd.learn.mysplash.notification.NotificationUtils
import com.psd.learn.mysplash.runSuspendCatching
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.util.UUID
import kotlin.properties.Delegates

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    private val gson: Gson,
    private val unSplashApiService: UnSplashApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private lateinit var requestDownloadInfo: RequestInfo
    private var downloading = false
    private var totalBytes by Delegates.notNull<Long>()
    private var totalProgress = 0
    private var downloadedBytes = 0L
    private val notificationManager by lazy { context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager }

    override suspend fun doWork(): Result {
        try {
            val jsonData = inputData.getString(INPUT_DOWNLOAD_DATA_INFO)
            requestDownloadInfo = gson.fromJson(jsonData, RequestInfo::class.java)
        } catch (exception: Exception) {
            Log.d("sangpd", "doWork - Failed to parse download data: $exception")
            onFailure()
            return Result.failure()
        }

        setForeground(getForegroundInfo())

        totalBytes = getTotalStreamSize(requestDownloadInfo)
        requestDownloadInfo.listItem.forEach { downloadItem ->
            downloading = true
            runSuspendCatching {
                executeDownload(downloadItem)
            }.fold(
                onSuccess = {
                    downloading = false
                    requestDownloadInfo.downloadedFile++
                },
                onFailure = {
                    Log.d("sangpd", "doWork - failed to download ${downloadItem.fileName} - exception: $it ")
                    downloading = false
                    onFailure()
                    return Result.failure()
                }
            )

            while (downloading) {
                if (isStopped) {
                    onFailure()
                    break
                }
            }
        }
        onSuccess()
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notification = NotificationUtils.getDownloadNotification(context, requestDownloadInfo, id)
        return ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }

    private suspend fun getTotalStreamSize(requestInfo: RequestInfo): Long = withContext(dispatcher) {
        requestInfo.listItem.sumOf { item -> unSplashApiService.openUrl(item.url).contentLength() }
    }

    private suspend fun executeDownload(downloadInfo: DownloadItem): Result {
        return withContext(dispatcher) {
            val responseBody = unSplashApiService.openUrl(downloadInfo.url)
            val uri = responseBody.saveImage(context, downloadInfo.fileName)
            if (uri != null) {
                return@withContext Result.success()
            }
            return@withContext Result.failure()
        }
    }

    private suspend fun onProgress(info: ProgressInfo) {
        if (!isStopped && !requestDownloadInfo.isFinish()) {
            val progress = ((downloadedBytes + info.bytesCopied) * 100 / totalBytes).toInt()
            Log.d("sangpd", "onProgress_currentProgress: $progress - totalProgress: $totalProgress - info.bytesCopied: ${info.bytesCopied} - info.progress: ${info.progress}")
            if (progress > totalProgress) {
                delay(100)
                if (info.progress == 100) {
                    downloadedBytes += info.bytesCopied
                }
                requestDownloadInfo.currentProgress = progress
                notifyStatus(DownloadStatus.DOWNLOADING, NOTIFICATION_ID)
                totalProgress = progress
            }
        }
    }

    private suspend fun onSuccess() {
        withContext(NonCancellable) {
            notifyStatus(DownloadStatus.COMPLETED)
        }
    }

    private suspend fun onFailure() {
        withContext(NonCancellable) {
            notifyStatus(DownloadStatus.CANCELLED)
            notificationManager.cancel(NOTIFICATION_ID)
        }
    }

    private fun notifyStatus(status: DownloadStatus, downloadId: Int = -1) {
        requestDownloadInfo.apply {
            downloadStatus = status
            when (status) {
                DownloadStatus.CANCELLED -> return
                DownloadStatus.COMPLETED -> {
                    currentProgress = 100
                }
                else -> {}
            }
        }
        val notification = NotificationUtils.getDownloadNotification(context, requestDownloadInfo, id)
        val notificationId = if (downloadId != -1) downloadId else id.hashCode()
        notificationManager.notify(notificationId, notification)
    }

    private suspend fun ResponseBody.saveImage(context: Context, fileName: String): Uri? {

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.TITLE, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Images.Media.SIZE, contentLength())
            put(MediaStore.Images.Media.RELATIVE_PATH, MY_SPLASH_RELATIVE_PATH)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        return if (uri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                byteStream().copyTo(outputStream, contentLength()).collect { progressInfo -> onProgress(progressInfo) }
            }
            uri
        } else {
            null
        }
    }

    companion object {

        private const val INPUT_DOWNLOAD_DATA_INFO = "input_data_download"
        private const val NOTIFICATION_ID = 200
        fun enQueueDownload(
            context: Context,
            gson: Gson,
            requestInfo: RequestInfo
        ): UUID {

            val jsonData = gson.toJson(requestInfo)
            val inputData = workDataOf(INPUT_DOWNLOAD_DATA_INFO to jsonData)

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build()

            val request = OneTimeWorkRequestBuilder<DownloadWorker>()
                .addTag(DownloadWorker::class.java.simpleName)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(request)
            return request.id
        }
    }
}