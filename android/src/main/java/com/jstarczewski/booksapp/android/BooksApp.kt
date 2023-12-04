package com.jstarczewski.booksapp.android

import android.app.Application
import com.jstarczewski.booksapp.setup

class BooksApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setup(this)
    }
}