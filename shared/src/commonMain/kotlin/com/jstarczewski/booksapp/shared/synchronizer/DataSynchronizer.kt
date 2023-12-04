package com.jstarczewski.booksapp.shared.synchronizer

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

interface DataSynchronizer {

    val isSyncing: Flow<Boolean>

    fun requestSync()
}
