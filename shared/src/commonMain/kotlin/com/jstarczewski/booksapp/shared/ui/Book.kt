package com.jstarczewski.booksapp.shared.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp)
    ) {
        KamelImage(
            modifier = Modifier.weight(1f),
            resource = asyncPainterResource(imageUrl),
            contentDescription = null
        )
        Column(modifier = Modifier.weight(3f)) {
            Text(title)
            Text(author)
            Text(epoch)
        }
    }
}