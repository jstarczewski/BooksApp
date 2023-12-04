package com.jstarczewski.booksapp.shared.db

import app.cash.sqldelight.db.SqlDriver
import com.jstarczewski.booksapp.WolneLekturyDatabse

expect class DriverFactory {

    fun createDriver(): SqlDriver
}

expect fun createDatabase(driverFactory: DriverFactory): WolneLekturyDatabse
