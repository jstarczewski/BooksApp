package com.jstarczewski.booksapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import moe.tlaster.precompose.molecule.collectAction

@Composable
expect fun booksRepository(): BooksRepository

@Stable
data class BooksModel(
    val books: List<DomainBook>
)

@Composable
fun BooksPresenter(
    flow: Flow<List<DomainBook>>
): BooksModel {

    val books: State<List<DomainBook>> = flow.collectAsState(emptyList())

    return BooksModel(books.value)
}