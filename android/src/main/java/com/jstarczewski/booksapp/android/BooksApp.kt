package com.jstarczewski.booksapp.android

import android.app.Application
import com.jstarczewski.booksapp.init
import com.jstarczewski.booksapp.sync.SyncUtil

class BooksApp : Application() {
    override fun onCreate() {
        super.onCreate()
        init()
        SyncUtil(this).requestSync()
    }
}