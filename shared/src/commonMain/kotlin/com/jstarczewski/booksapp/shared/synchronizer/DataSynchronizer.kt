package com.jstarczewski.booksapp.shared.synchronizer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

interface DataSynchronizer {

    val isSyncing: Flow<Boolean>

    fun requestSync()
}

@Inject
abstract class DataSynchronizerProxy(
    private val syncManager: SyncManager,
    private val delegate: DataSynchronizer,
    private val period: Duration,
    private val group: SyncGroup,
    private val coroutineScope: CoroutineScope
) : DataSynchronizer {
    override val isSyncing: Flow<Boolean> = delegate.isSyncing

    override fun requestSync() {
        coroutineScope.launch {
            syncManager.syncGroupIfOlderThan(
                group = group,
                other = Clock.System.now().minus(period)
            ) {
                delegate.requestSync()
            }
        }
    }
}

@Inject
class BooksDataSynchronizer(
    syncManager: SyncManager,
    delegate: DataSynchronizer
) : DataSynchronizerProxy(
    syncManager = syncManager,
    delegate = delegate,
    period = 20.minutes,
    group = SyncGroup.BOOKS,
    coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
)