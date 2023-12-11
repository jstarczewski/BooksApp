package com.jstarczewski.booksapp.books

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jstarczewski.booksapp.favourites.FavouritesRepository
import com.jstarczewski.booksapp.shared.ui.Book
import moe.tlaster.precompose.molecule.rememberPresenter
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

fun RouteBuilder.booksScene(
    booksRepository: BooksRepository,
    favouritesRepository: FavouritesRepository,
    moveToDetails: (String) -> Unit
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
        BooksScene(books) {
            moveToDetails(it)
        }
    }
}

@Composable
fun BooksScene(
    books: BooksModel, onClick: (key: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(books.books, key = { it.key }) {
                Book(
                    modifier = Modifier.clickable { onClick(it.key) },
                    title = it.name,
                    author = it.author,
                    imageUrl = it.thumbnailUrl.orEmpty()
                )
            }
        }
    }
}