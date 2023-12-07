package com.jstarczewski.booksapp

import android.content.Context

fun setup(context: Context) {
    setupInternally {
        createAppComponent(context)
    }
}