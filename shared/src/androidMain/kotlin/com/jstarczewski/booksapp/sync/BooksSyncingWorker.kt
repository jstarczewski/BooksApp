package com.jstarczewski.booksapp.sync

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.jstarczewski.booksapp.R
import com.jstarczewski.booksapp.createAppComponent

class BooksSyncingWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val booksService = createAppComponent(appContext).booksService

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, appContext.createNotification(CHANNEL_ID)
        )
    }

    override suspend fun doWork(): Result {
        booksService.downloadAllBooks()
        return Result.success()
    }

    private fun Context.createNotification(
        channelId: String
    ): Notification {
        val channel = NotificationChannel(
            channelId,
            "Channel name",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Essa"
        }
        // Register the channel with the system
        val notificationManager: NotificationManager? =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        notificationManager?.createNotificationChannel(channel)

        return NotificationCompat.Builder(
            this,
            channelId,
        )
            .setSmallIcon(
                R.drawable.icon
            )
            .setContentTitle("Syncing books")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}

private const val NOTIFICATION_ID = 123

private const val CHANNEL_ID = "channel_id_123"

