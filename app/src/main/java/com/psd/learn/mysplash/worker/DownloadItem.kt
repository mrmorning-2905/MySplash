package com.psd.learn.mysplash.worker

import android.os.Parcelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.InputStream
import java.io.OutputStream
import kotlin.concurrent.fixedRateTimer

@Parcelize
data class DownloadItem(
    val url: String,
    val fileName: String,
    val photoId: String
) : Parcelable

@Parcelize
data class RequestInfo(
    val totalFiles: Int,
    val totalSize: Long = 0L,// todo handle at selected files
    val listItem: List<DownloadItem>
) : Parcelable {
    @IgnoredOnParcel
    var currentProgress: Int = 0

    @IgnoredOnParcel
    var downloadedFile: Int = 0

    @IgnoredOnParcel
    var downloadStatus: DownloadStatus = DownloadStatus.QUEUED

    fun isFinish(): Boolean = downloadStatus in DownloadStatus.finished
    fun isRunning(): Boolean = downloadStatus in DownloadStatus.running
}

data class ProgressInfo(
    val progress: Int = 0,
    val bytesCopied: Long = 0,
    val speed: Long = 0
)

enum class DownloadStatus {
    DOWNLOADING,
    FAILED,
    CANCELLED,
    COMPLETED,
    QUEUED;

    companion object {
        val finished = listOf(FAILED, CANCELLED, COMPLETED)
        val running = listOf(QUEUED, DOWNLOADING)
    }
}

fun InputStream.copyTo(out: OutputStream, streamSize: Long): Flow<ProgressInfo> {
    return flow {
        var bytesCopied: Long = 0
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = read(buffer)

        var lastTotalBytesRead = 0L
        var speed: Long = 0

        @Suppress("KotlinConstantConditions")
        val timer = fixedRateTimer("timer", true, 0L, 1000) {
            val totalBytesRead = bytesCopied
            speed = totalBytesRead - lastTotalBytesRead
            lastTotalBytesRead = totalBytesRead
        }

        while (bytes >= 0) {
            out.write(buffer, 0, bytes)
            bytesCopied += bytes
            emit(ProgressInfo((bytesCopied * 100 / streamSize).toInt(), bytesCopied, speed))
            bytes = read(buffer)
        }
        timer.cancel()
    }
        .flowOn(Dispatchers.IO)
}