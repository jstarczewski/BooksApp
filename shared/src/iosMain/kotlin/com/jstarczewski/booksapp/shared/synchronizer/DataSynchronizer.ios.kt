package com.jstarczewski.booksapp.shared.synchronizer

import com.jstarczewski.booksapp.books.BooksService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundTaskIdentifier


class ContinuingInBackgroundBooksDataSynchronizer(
    private val booksService: BooksService
) : DataSynchronizer {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _isSyncing = MutableStateFlow(true)

    override val isSyncing: Flow<Boolean> = _isSyncing

    override fun requestSync() {
        _isSyncing.value = true

        val job = scope.launch {
            booksService.downloadAllBooks()
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