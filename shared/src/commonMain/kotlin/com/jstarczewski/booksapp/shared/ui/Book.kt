package com.jstarczewski.booksapp.shared.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun Book(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String,
    author: String,
    epoch: String
) {
    Column(
        modifier = modifier
            .width(200.dp)
            .padding(10.dp)
    ) {
        KamelImage(
            modifier = Modifier
                .height(300.dp),
            resource = asyncPainterResource(imageUrl),
            contentDescription = null
        )
        Text(title)
        Text(author)
        Text(epoch)
    }
}