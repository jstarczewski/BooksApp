package com.jstarczewski.booksapp.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jstarczewski.booksapp.books.BooksRepository
import com.jstarczewski.booksapp.books.DomainBook
import com.jstarczewski.booksapp.books.FavouritesAction
import com.jstarczewski.booksapp.shared.ui.Book
import moe.tlaster.precompose.molecule.producePresenter
import moe.tlaster.precompose.molecule.rememberPresenter
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

const val FAVOURITES_ROUTE = "/favourites"

fun RouteBuilder.favouritesScene(
    favouritesRepository: FavouritesRepository,
    booksRepository: BooksRepository
) {
    scene(
        route = FAVOURITES_ROUTE,
        navTransition = NavTransition()
    ) {
        val (state, action) = rememberPresenter {
            FavouritesPresenter(
                favouritesRepository = favouritesRepository,
                booksRepository = booksRepository,
                it
            )
        }
        FavouritesScene(state.favourites) {
            action.trySend(FavouritesAction.Remove(it))
        }
    }
}

@Composable
private fun FavouritesScene(
    books: List<DomainBook>,
    onClick: (key: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            items(books) {
                Book(
                    modifier = Modifier.clickable { onClick(it.key) },
                    title = it.name,
                    author = it.author,
                    imageUrl = it.thumbnailUrl.orEmpty(),
                    epoch = it.epoch.orEmpty()
                )
            }
        }
    }
}