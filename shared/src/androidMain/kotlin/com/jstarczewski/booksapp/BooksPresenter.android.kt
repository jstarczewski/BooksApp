package com.jstarczewski.booksapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun booksRepository(): BooksRepository {
    val context = LocalContext.current
    return remember(context) { BooksRepository(createDatabase(DriverFactory(context))) }
}