package com.jstarczewski.booksapp.shared.db

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jstarczewski.booksapp.DriverFactory
import com.jstarczewski.booksapp.WolneLekturyDatabse
import com.jstarczewski.booksapp.createDatabase

@Composable
actual fun WolneLekturyDatabase(): WolneLekturyDatabse =
    remember { createDatabase(DriverFactory()) }