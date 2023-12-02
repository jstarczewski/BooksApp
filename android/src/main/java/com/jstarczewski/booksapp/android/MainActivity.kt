package com.jstarczewski.booksapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.jstarczewski.booksapp.BooksApp
import com.jstarczewski.booksapp.sync.SyncUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val syncUtil = remember(context) { SyncUtil(context) }
            val isSyncing by syncUtil.isSyncing.collectAsState(initial = true)
            if (isSyncing.not()) {
                BooksApp()
            } else {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}