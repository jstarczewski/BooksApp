package com.jstarczewski.booksapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jstarczewski.booksapp.BooksAppTheme
import com.jstarczewski.booksapp.shared.ui.Book

@Preview
@Composable
fun BookPreview() {
    BooksAppTheme {
        Book(
            modifier = Modifier,
            "20 000 mil podmorskiej żeglugi",
            "https://wolnelektury.pl/media/book/cover_api_thumb/20-000-mil-podmorskiej-zeglugi_4tC1gOu.jpg",
            "Jules Gabriel Verne",
            "Pozytywizm"
        )
    }
}