package com.jstarczewski.booksapp.shared.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.jstarczewski.booksapp.WolneLekturyDatabse

actual class DriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(WolneLekturyDatabse.Schema, context, "lektury.db")
}

actual fun createDatabase(driverFactory: DriverFactory): WolneLekturyDatabse =
    WolneLekturyDatabse.invoke(driverFactory.createDriver())