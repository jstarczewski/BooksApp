package com.jstarczewski.booksapp.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import moe.tlaster.precompose.molecule.producePresenter
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

private const val BASE_DETAILS_ROUTE = "/details"

private const val PATH_ARGUMENT = "fullSortKey"

const val DETAILS_ROUTE = "$BASE_DETAILS_ROUTE/{$PATH_ARGUMENT}"

fun detailsPath(fullSortKey: String) = "$BASE_DETAILS_ROUTE/$fullSortKey"

fun RouteBuilder.detailsScene(
    detailsRepository: DetailsRepository
) {
    scene(
        route = DETAILS_ROUTE,
        navTransition = NavTransition()
    ) { backStackEntry ->
        val fullSortKey: String? = backStackEntry.path(PATH_ARGUMENT)
        if (fullSortKey != null) {
            val details by producePresenter {
                BookDetailsPresenter(
                    detailsRepository = detailsRepository,
                    fullSortKey = fullSortKey
                )
            }
            if (details is BookDetailsModel.BookDetails) {
                DetailsScene(details as BookDetailsModel.BookDetails)
            }
        } else {
            throw IllegalStateException("No full sort key on navigation")
        }
    }
}

@Composable
fun DetailsScene(
    details: BookDetailsModel.BookDetails
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(details.title)
        Text(details.shortDescription)
        Text(details.longDescription)
    }
}