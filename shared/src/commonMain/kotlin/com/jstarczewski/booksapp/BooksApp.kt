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
import com.jstarczewski.booksapp.ui.Book
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.molecule.producePresenter
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun BooksApp() {
    PreComposeApp {
        BooksAppTheme {
            val navigator = rememberNavigator()
            NavHost(
                navigator = navigator,
                navTransition = NavTransition(),
                initialRoute = "/books"
            ) {
                scene(
                    route = "/books",
                    navTransition = NavTransition()
                ) {
                    val repository = booksRepository()
                    val books by producePresenter { BooksPresenter(repository.f) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                        LazyRow(
                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        ) {
                            items(books.books) {
                                Book(
                                    title = it.name,
                                    author = it.author,
                                    imageUrl = it.thumbnailUrl.orEmpty(),
                                    epoch = it.epoch.orEmpty()
                                )
                            }
                        }
                    }
                }
                scene(
                    route = "/favourites",
                    navTransition = NavTransition()
                ) {

                }
            }
        }
    }
}