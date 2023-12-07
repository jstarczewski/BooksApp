package com.jstarczewski.booksapp.books

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jstarczewski.booksapp.shared.api.ApiClient
import com.jstarczewski.booksapp.Book
import com.jstarczewski.booksapp.shared.api.BookApiModel
import com.jstarczewski.booksapp.SelectAuthorsWithMostBooks
import com.jstarczewski.booksapp.WolneLekturyDatabse
import com.jstarczewski.booksapp.shared.api.authors
import com.jstarczewski.booksapp.shared.api.getBooks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

data class DomainBook(
    val key: String,
    val name: String,
    val author: String,
    val thumbnailUrl: String?,
    val epoch: String?
)

fun Book.asDomainBook(): DomainBook = let {
    DomainBook(
        key = it.full_sort_key,
        name = it.title,
        author = it.author,
        thumbnailUrl = it.ThumbnailUrl,
        epoch = it.Epoch
    )
}

fun BookApiModel.asDomainBook(): DomainBook = let {
    DomainBook(
        key = it.full_sort_key,
        name = it.title,
        author = it.author,
        thumbnailUrl = it.simple_thumb,
        epoch = it.epoch
    )
}

fun BookApiModel.asBook() = let {
    Book(
        full_sort_key = it.full_sort_key,
        title = it.title,
        author = it.author,
        ThumbnailUrl = it.simple_thumb,
        Epoch = it.epoch
    )
}


@Inject
class BooksRepository(
    database: WolneLekturyDatabse,
) {

    val books by lazy {
        database.bookQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map {
                it.map { it.asDomainBook() }
            }
    }
}

@Inject
class BooksService(
    private val database: WolneLekturyDatabse,
    private val apiClient: ApiClient
) {
    suspend fun downloadAllBooks() = withContext(Dispatchers.IO) {
        apiClient.getBooks().forEach { apiBook ->
            database.bookQueries.insertFullBookObject(apiBook.asBook())
        }
    }
}