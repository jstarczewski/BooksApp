package com.jstarczewski.booksapp

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun BooksAppViewController(): UIViewController = ComposeUIViewController {
    BooksApp()
}