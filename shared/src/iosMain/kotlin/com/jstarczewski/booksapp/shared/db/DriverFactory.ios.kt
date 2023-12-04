package com.jstarczewski.booksapp.shared.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.jstarczewski.booksapp.WolneLekturyDatabse

actual class DriverFactory {

    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(WolneLekturyDatabse.Schema, "lektury.db")
}

actual fun createDatabase(driverFactory: DriverFactory): WolneLekturyDatabse = database

val database = WolneLekturyDatabse.invoke(DriverFactory().createDriver())
