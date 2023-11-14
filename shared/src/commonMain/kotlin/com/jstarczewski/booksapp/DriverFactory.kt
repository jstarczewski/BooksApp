package com.jstarczewski.booksapp

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {

    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): WolneLekturyDatabse =
    WolneLekturyDatabse.invoke(driverFactory.createDriver())
