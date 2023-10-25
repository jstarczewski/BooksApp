package com.jstarczewski.booksapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BooksApp() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Hello World")
    }
}