package com.jstarczewski.booksapp.shared.db

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jstarczewski.booksapp.DriverFactory
import com.jstarczewski.booksapp.WolneLekturyDatabse
import com.jstarczewski.booksapp.books.BooksRepository
import com.jstarczewski.booksapp.createDatabase

@Composable
actual fun WolneLekturyDatabase(): WolneLekturyDatabse {
    val context = LocalContext.current
    return remember(context) { createDatabase(DriverFactory(context)) }
}