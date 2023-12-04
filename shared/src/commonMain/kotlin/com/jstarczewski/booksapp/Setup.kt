package com.jstarczewski.booksapp

import com.jstarczewski.booksapp.shared.synchronizer.DataSynchronizer
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default

internal fun setupInternally(dataSynchronizer: DataSynchronizer) {
    dataSynchronizer.requestSync()
    KamelConfig {
        takeFrom(KamelConfig.Default)
    }
}