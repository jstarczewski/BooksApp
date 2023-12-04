package com.jstarczewski.booksapp.details

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jstarczewski.booksapp.WolneLekturyDatabse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.mapNotNull
import me.tatarka.inject.annotations.Inject

@Inject
class DetailsRepository(
    private val db: WolneLekturyDatabse,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getBookDetails(fullSortKey: String) =
        db.bookDetailQueries.selectDetails(fullSortKey)
            .asFlow()
            .mapToList(dispatcher)
            .mapNotNull { it.firstOrNull() }
}