package com.jstarczewski.booksapp

import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default

fun init() {
    KamelConfig {
        takeFrom(KamelConfig.Default)
    }
}