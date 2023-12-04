package com.jstarczewski.booksapp.shared.synchronizer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jstarczewski.booksapp.shared.api.ApiClient
import com.jstarczewski.booksapp.books.BooksSynchronizer
import com.jstarczewski.booksapp.shared.db.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundTaskIdentifier


class ContinuingInBackgroundBooksDataSynchronizer(
    private val synchronizer: BooksSynchronizer
) : DataSynchronizer {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _isSyncing = MutableStateFlow(true)

    override val isSyncing: Flow<Boolean> = _isSyncing

    override fun requestSync() {
        _isSyncing.value = true

        val job = scope.launch {
            synchronizer.syncIfOlderThan()
        }

        var idenitifier: UIBackgroundTaskIdentifier? = null

        idenitifier = UIApplication.sharedApplication.beginBackgroundTaskWithName("books") {
            job.cancel()
            UIApplication.sharedApplication.endBackgroundTask(idenitifier!!)
            idenitifier = null
            _isSyncing.value = false
        }

        job.invokeOnCompletion {
            UIApplication.sharedApplication.endBackgroundTask(idenitifier!!)
            idenitifier = null
            _isSyncing.value = false
        }
    }
}