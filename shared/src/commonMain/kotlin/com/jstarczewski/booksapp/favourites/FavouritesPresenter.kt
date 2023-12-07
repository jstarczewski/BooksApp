package com.jstarczewski.booksapp.favourites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jstarczewski.booksapp.Book
import com.jstarczewski.booksapp.books.BooksRepository
import com.jstarczewski.booksapp.books.DomainBook
import com.jstarczewski.booksapp.books.FavouritesAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import moe.tlaster.precompose.molecule.collectAction

data class FavouritesModel(
    val favourites: List<DomainBook>
)

@Composable
fun FavouritesPresenter(
    favouritesRepository: FavouritesRepository,
    booksRepository: BooksRepository,
    action: Flow<FavouritesAction>
): FavouritesModel {

    action.collectAction {
        when (this) {
            is FavouritesAction.Add -> favouritesRepository.addToFavourites(fullSortKey)
            is FavouritesAction.Remove -> favouritesRepository.removeFavourite(fullSortKey)
        }
    }

    return FavouritesModel(favourites = emptyList())
}