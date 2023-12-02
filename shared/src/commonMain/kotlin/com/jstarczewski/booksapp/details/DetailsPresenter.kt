package com.jstarczewski.booksapp.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Stable
sealed interface BookDetailsModel {

    @Stable
    data object None : BookDetailsModel

    @Stable
    data class BookDetails(
        val title: String,
        val shortDescription: String,
        val longDescription: String,
    ) : BookDetailsModel
}

@Composable
fun BookDetailsPresenter(
    detailsRepository: DetailsRepository,
    fullSortKey: String
): BookDetailsModel {

    val details by detailsRepository.getBookDetails(fullSortKey).collectAsState(null)

    return details?.let {
        BookDetailsModel.BookDetails(
            title = it.FullSortKey,
            shortDescription = it.ShortDescription,
            longDescription = it.LongDescription
        )
    } ?: BookDetailsModel.None
}