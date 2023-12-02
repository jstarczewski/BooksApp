package com.jstarczewski.booksapp.books

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.jstarczewski.booksapp.favourites.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import moe.tlaster.precompose.molecule.collectAction

@Stable
data class BooksModel(
    val books: List<DomainBook>
)

sealed interface FavouritesAction {

    class Add(val fullSortKey: String) : FavouritesAction

    class Remove(val fullSortKey: String) : FavouritesAction
}

@Composable
fun BooksPresenter(
    booksRepository: BooksRepository,
    favouritesRepository: FavouritesRepository,
    action: Flow<FavouritesAction>
): BooksModel {

    val books: State<List<DomainBook>> = booksRepository.books.collectAsState(emptyList())

    action.collectAction {
        when (this) {
            is FavouritesAction.Add -> favouritesRepository.addToFavourites(fullSortKey)
            is FavouritesAction.Remove -> favouritesRepository.removeFavourite(fullSortKey)
        }
    }

    return BooksModel(books.value)
}