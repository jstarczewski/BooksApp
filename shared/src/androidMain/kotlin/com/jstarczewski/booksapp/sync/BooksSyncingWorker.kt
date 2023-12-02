package com.jstarczewski.booksapp.sync

import android.app.Notification
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.jstarczewski.booksapp.ApiClient
import com.jstarczewski.booksapp.DriverFactory
import com.jstarczewski.booksapp.books.BooksSynchronizer
import com.jstarczewski.booksapp.createDatabase
import kotlin.time.Duration.Companion.minutes

class BooksSyncingWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification(appContext, CHANNEL_ID)
        )
    }

    override suspend fun doWork(): Result {
        val synchronizer = createBooksSynchronizer(appContext)
        synchronizer.syncIfOlderThan(1.minutes)
        return Result.success()
    }

    private fun createNotification(
        context: Context,
        channelId: String
    ) = Notification.Builder(context, channelId)
        .setContentTitle("Syncing Books")
        .setContentText("Books are being synced right now")
        .build()
}

private const val NOTIFICATION_ID = 123

private const val CHANNEL_ID = "channel_id_123"


private fun createBooksSynchronizer(context: Context) = BooksSynchronizer(
    db = createDatabase(DriverFactory(context)),
    apiClient = ApiClient()
)

