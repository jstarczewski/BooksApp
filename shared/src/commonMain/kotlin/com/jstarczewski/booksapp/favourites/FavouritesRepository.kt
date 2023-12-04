package com.jstarczewski.booksapp.favourites

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jstarczewski.booksapp.WolneLekturyDatabse
import com.jstarczewski.booksapp.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

data class Favourite(
    val fullSortKey: String,
    val userId: Long
)

@Inject
class FavouritesRepository(
    private val wolneLekturyDatabse: WolneLekturyDatabse,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    val favourites: Flow<List<Favourite>> = wolneLekturyDatabse
        .favouriteQueries
        .selectAllFavourites(1)
        .asFlow()
        .mapToList(dispatcher)
        .map { values -> values.map { Favourite(it.FullSortKey, it.UserId) } }

    fun addToFavourites(
        fullSortKey: String
    ) = wolneLekturyDatabse.favouriteQueries.addFavourite(
        FullSortKey = fullSortKey,
        UserId = 1
    )

    fun removeFavourite(
        fullSortKey: String,
    ) = wolneLekturyDatabse.favouriteQueries.removeFavourite(
        FullSortKey = fullSortKey,
        UserId = 1
    )
}