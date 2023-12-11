package com.jstarczewski.booksapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jstarczewski.booksapp.BooksAppTheme
import com.jstarczewski.booksapp.shared.ui.Book

@Preview
@Composable
fun BookPreview() {
    BooksAppTheme {
        Book(
            modifier = Modifier.width(200.dp),
            "20 000 mil podmorskiej żeglugi",
            "https://wolnelektury.pl/media/book/cover_api_thumb/20-000-mil-podmorskiej-zeglugi_4tC1gOu.jpg",
            "Jules Gabriel Verne"
        )
    }
}
