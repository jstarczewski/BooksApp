package com.jstarczewski.booksapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jstarczewski.booksapp.BooksApp
import com.jstarczewski.booksapp.appComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BooksApp(appComponent = appComponent)
        }
    }
}