package com.jstarczewski.booksapp.shared.synchronizer

import com.jstarczewski.booksapp.Sync
import com.jstarczewski.booksapp.WolneLekturyDatabse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import me.tatarka.inject.annotations.Inject

sealed interface SyncState {

    data object Never : SyncState

    data class Exact(
        val id: Int,
        val timestamp: Instant,
        val finished: Boolean
    ) : SyncState
}

fun SyncState.Exact.isOlderThan(instant: Instant) = timestamp < instant

enum class SyncGroup {

    BOOKS;
}

@Inject
class SyncManager(
    private val database: WolneLekturyDatabse
) {
    suspend fun getLastSyncState(syncGroup: SyncGroup): SyncState = withContext(Dispatchers.IO) {

        val sync = database.dataSyncQueries.selectLatestSyncForGroup(syncGroup.name)
            .executeAsOneOrNull()

        if (sync != null) {
            SyncState.Exact(
                id = sync.Id.toInt(),
                timestamp = LocalDateTime.parse(sync.datetime.replace(" ", "T")).toInstant(TimeZone.UTC),
                finished = sync.State?.toInt() == 1
            )
        } else {
            SyncState.Never
        }
    }

    suspend fun startSync(syncGroup: SyncGroup) = withContext(Dispatchers.IO) {
        database.dataSyncQueries.insertSyncStart(syncGroup.name)
        val sync =
            database.dataSyncQueries
                .selectLatestSyncForGroup(syncGroup.name)
                .executeAsOne()
        SyncState.Exact(
            id = sync.Id.toInt(),
            timestamp = LocalDateTime.parse(sync.datetime.replace(" ", "T")).toInstant(TimeZone.UTC),
            finished = sync.State?.toInt() == 1
        )
    }

    suspend fun finishSyncWithSuccess(id: Int) = withContext(Dispatchers.IO) {
        database.dataSyncQueries.updateSyncStateAsSuccess(id.toLong())
    }
}

suspend fun SyncManager.syncGroupIfOlderThan(
    group: SyncGroup,
    other: Instant,
    operation: suspend () -> Unit
): Unit = withContext(Dispatchers.IO) {
    when (val latestSync = getLastSyncState(syncGroup = group)) {
        is SyncState.Exact -> {
            if (latestSync.isOlderThan(other)) {
                startSync(group)
                operation()
                finishSyncWithSuccess(latestSync.id)
            }
        }

        SyncState.Never -> {
            startSync(group)
            operation()
            val firstSync = getLastSyncState(group) as? SyncState.Exact
            firstSync?.let {
                finishSyncWithSuccess(firstSync.id)
            }
        }
    }
}