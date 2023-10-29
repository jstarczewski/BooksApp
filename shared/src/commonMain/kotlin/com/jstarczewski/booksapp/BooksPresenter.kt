package com.jstarczewski.booksapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow

@Stable
data class BooksModel(
    val books: List<BookApiModel>
)

@Composable
fun BooksPresenter(
    booksFlow: Flow<List<BookApiModel>>
): BooksModel {

    val books: State<List<BookApiModel>> =
        booksFlow.collectAsState(emptyList())

    return BooksModel(books.value)
}