package com.jstarczewski.booksapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jstarczewski.booksapp.books.BooksService
import com.jstarczewski.booksapp.shared.api.ApiClient
import com.jstarczewski.booksapp.shared.db.DriverFactory
import com.jstarczewski.booksapp.shared.db.createDatabase
import com.jstarczewski.booksapp.shared.synchronizer.ContinuingInBackgroundBooksDataSynchronizer

@Composable
actual fun createAppComponent(): AppComponent = remember { AppComponent() }

fun AppComponent(): AppComponent {
    val database = createDatabase(DriverFactory())
    val apiClient = ApiClient()
    val dataSynchronizer = ContinuingInBackgroundBooksDataSynchronizer(
        BooksService(
            database, apiClient
        )
    )
    return AppComponent::class.create(
        wolneLekturyDatabse = database,
        apiClient = apiClient,
        dataSynchronizer = dataSynchronizer
    )
}