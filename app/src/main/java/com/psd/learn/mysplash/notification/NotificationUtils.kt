package com.psd.learn.mysplash.notification

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import com.psd.learn.mysplash.MY_SPLASH_RELATIVE_PATH
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.worker.DownloadStatus
import com.psd.learn.mysplash.worker.RequestInfo
import java.util.UUID

object NotificationUtils {

    private const val DOWNLOADS_CHANNEL_ID = "downloads_channel_id"
    private const val ACTION_LAUNCH_SAMSUNG_MYFILE = "samsung.myfiles.intent.action.LAUNCH_MY_FILES"
    private const val EXTRA_START_PATH = "samsung.myfiles.intent.extra.START_PATH"
    private const val SAMSUNG_MYFILE_PACKAGE = "com.sec.android.app.myfiles"

    fun createNotificationChannel(context: Context) {
        // create many notification channels for many purpose
        val notificationManager = context.getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
        val channelList = listOf(
            NotificationChannel(
                DOWNLOADS_CHANNEL_ID,
                "Downloads",
                NotificationManager.IMPORTANCE_LOW
            )
        )
        notificationManager.createNotificationChannels(channelList)
    }

    private fun getPendingIntent(context: Context, uri: Uri?, numberDownloadFiles: Int): PendingIntent {
        val intent = Intent()
        val pendingFlags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        when (numberDownloadFiles) {
            1 -> {
                intent.apply {
                    action = Intent.ACTION_VIEW
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    setDataAndType(uri, "image/*")
                }
                val chooser = Intent.createChooser(intent, "Open with")
                return PendingIntent.getActivity(context, 0, chooser, pendingFlags)
            }

            else -> {
                intent.apply {
                    action = ACTION_LAUNCH_SAMSUNG_MYFILE
                    putExtra(EXTRA_START_PATH, MY_SPLASH_RELATIVE_PATH)
                    setPackage(SAMSUNG_MYFILE_PACKAGE)
                }
                return PendingIntent.getActivity(context, 0, intent, pendingFlags)
            }
        }
    }

    fun getDownloadNotification(
        context: Context,
        requestInfo: RequestInfo,
        workId: UUID
    ): Notification {
        val builder = NotificationCompat.Builder(context, DOWNLOADS_CHANNEL_ID).apply {
            setContentTitle(if (requestInfo.totalFiles > 1) "Download selected images" else requestInfo.listItem[0].fileName)
            color = ContextCompat.getColor(context, R.color.colorBlue)
        }

        Log.d("sangpd", "getDownloadNotification_downloadStatus: ${requestInfo.downloadStatus}")
        when (requestInfo.downloadStatus) {
            DownloadStatus.CANCELLED -> {
                builder.apply {
                    setSmallIcon(R.drawable.ic_download_cancel)
                    setContentText("Cancel")
                    color = Color.RED
                    setCategory(Notification.CATEGORY_ERROR)
                }
            }

            DownloadStatus.FAILED -> {
                builder.apply {
                    setSmallIcon(R.drawable.ic_download_fail)
                    setContentText("Download failed")
                    color = Color.RED
                    setCategory(Notification.CATEGORY_ERROR)
                }
            }

            DownloadStatus.COMPLETED -> {
                builder.apply {
                    setSmallIcon(android.R.drawable.stat_sys_download_done)
                    setContentText("Download completed")
                    setAutoCancel(false)
                    setCategory(Notification.CATEGORY_STATUS)
                    setOngoing(true)
                    setProgress(0, 0, false)
                    setContentIntent(getPendingIntent(context, requestInfo.uri, requestInfo.totalFiles))
                }
            }

            DownloadStatus.DOWNLOADING, DownloadStatus.QUEUED -> {
                builder.apply {
                    setSmallIcon(android.R.drawable.stat_sys_download)
                    setContentText(
                        if (requestInfo.currentProgress == 0) {
                            "Preparing..."
                        } else {
                            "Downloading ${requestInfo.downloadedFile}/${requestInfo.totalFiles} files - ${requestInfo.currentProgress}/100%"
                        }
                    )
                    setOngoing(true)
                    setCategory(Notification.CATEGORY_PROGRESS)
                    setProgress(100, requestInfo.currentProgress, requestInfo.currentProgress == 0)
                    foregroundServiceBehavior = NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
                    addAction(
                        NotificationCompat.Action.Builder(
                            R.drawable.ic_download_cancel,
                            "Cancel",
                            WorkManager.getInstance(context).createCancelPendingIntent(workId)
                        ).build()
                    )
                }
            }
        }
        return builder.build()
    }
}