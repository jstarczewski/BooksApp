package com.jstarczewski.booksapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import com.jstarczewski.booksapp.ui.Book

@Composable
fun BooksApp() {
    BooksAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            val scope = rememberCoroutineScope()

            val books by scope.launchMolecule(mode = RecompositionMode.ContextClock) {
                BooksPresenter(ApiClient().booksFlow())
            }.collectAsState()

            LazyRow(
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            ) {
                items(books.books) {
                    Book(
                        title = it.title,
                        author = it.author,
                        imageUrl = it.simple_thumb
                    )
                }
            }
        }
    }
}