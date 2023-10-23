package com.jstarczewski.booksapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform