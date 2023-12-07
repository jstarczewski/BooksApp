package com.jstarczewski.booksapp

import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default

private var _appComponent: AppComponent? = null

val appComponent: AppComponent
    get() = _appComponent ?: throw IllegalStateException()

internal fun setupInternally(createAppComponent: () -> AppComponent) {
    _appComponent = createAppComponent()
    _appComponent?.run {
        booksDataSynchronizer.requestSync()
    }
    KamelConfig {
        takeFrom(KamelConfig.Default)
    }
}