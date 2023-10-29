package com.jstarczewski.booksapp.android

import android.app.Application
import com.jstarczewski.booksapp.init

class BooksApp : Application() {
    override fun onCreate() {
        super.onCreate()
        init()
    }
}