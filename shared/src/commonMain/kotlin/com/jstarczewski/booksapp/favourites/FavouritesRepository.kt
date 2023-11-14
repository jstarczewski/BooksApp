package com.jstarczewski.booksapp.favourites

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jstarczewski.booksapp.WolneLekturyDatabse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Favourite(
    val fullSortKey: String,
    val userId: Long
)

class FavouritesRepository(
    private val wolneLekturyDatabse: WolneLekturyDatabse,
    private val userId: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    val favourites: Flow<List<Favourite>> = wolneLekturyDatabse
        .favouriteQueries
        .selectAllFavourites(userId)
        .asFlow()
        .mapToList(dispatcher)
        .map { values -> values.map { Favourite(it.FullSortKey, it.UserId) } }

    fun addToFavourites(
        fullSortKey: String
    ) = wolneLekturyDatabse.favouriteQueries.addFavourite(
        FullSortKey = fullSortKey,
        UserId = userId
    )

    fun removeFavourite(
        fullSortKey: String,
    ) = wolneLekturyDatabse.favouriteQueries.removeFavourite(
        FullSortKey = fullSortKey,
        UserId = userId
    )
}