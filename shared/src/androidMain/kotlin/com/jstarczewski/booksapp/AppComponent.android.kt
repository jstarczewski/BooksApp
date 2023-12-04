package com.jstarczewski.booksapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jstarczewski.booksapp.shared.api.ApiClient
import com.jstarczewski.booksapp.shared.db.DriverFactory
import com.jstarczewski.booksapp.shared.db.createDatabase
import com.jstarczewski.booksapp.shared.synchronizer.WorkManagerBooksDataSynchronizer

@Composable
actual fun createAppComponent(): AppComponent {
    val context = LocalContext.current
    return remember(context) { createAppComponent(context) }
}

fun createAppComponent(
    context: Context
): AppComponent {
    val database = createDatabase(
        DriverFactory(
            context
        )
    )
    val apiClient = ApiClient()
    return AppComponent::class.create(
        wolneLekturyDatabse = database,
        apiClient = apiClient,
        dataSynchronizer = WorkManagerBooksDataSynchronizer(context)
    )
}
