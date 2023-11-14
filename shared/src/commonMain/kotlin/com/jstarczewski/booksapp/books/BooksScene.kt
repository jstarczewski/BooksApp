package com.jstarczewski.booksapp.books

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jstarczewski.booksapp.favourites.FavouritesRepository
import com.jstarczewski.booksapp.shared.ui.Book
import moe.tlaster.precompose.molecule.producePresenter
import moe.tlaster.precompose.molecule.rememberPresenter
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

fun RouteBuilder.booksScene(
    booksRepository: BooksRepository,
    favouritesRepository: FavouritesRepository,
    moveToFavourites: () -> Unit
) {
    scene(
        route = "/books", navTransition = NavTransition()
    ) {
        val (books, action) = rememberPresenter {
            BooksPresenter(
                booksRepository = booksRepository,
                favouritesRepository = favouritesRepository,
                action = it
            )
        }
        BooksScene(books, onFavourites = moveToFavourites) {
            action.trySend(FavouritesAction.Add(it))
        }
    }
}

@Composable
fun BooksScene(
    books: BooksModel,
    onFavourites: () -> Unit,
    onClick: (key: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            items(books.books) {
                Book(
                    modifier = Modifier.clickable { onClick(it.key) },
                    title = it.name,
                    author = it.author,
                    imageUrl = it.thumbnailUrl.orEmpty(),
                    epoch = it.epoch.orEmpty()
                )
            }
        }
        Button(onClick = { onFavourites() }) {
            Text("Favourites")
        }
    }
}