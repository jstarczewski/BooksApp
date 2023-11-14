package com.jstarczewski.booksapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jstarczewski.booksapp.books.BooksRepository
import com.jstarczewski.booksapp.books.booksScene
import com.jstarczewski.booksapp.favourites.FAVOURITES_ROUTE
import com.jstarczewski.booksapp.favourites.FavouritesRepository
import com.jstarczewski.booksapp.favourites.favouritesScene
import com.jstarczewski.booksapp.shared.db.WolneLekturyDatabase
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun BooksApp() {
    val database = WolneLekturyDatabase()
    val favouritesRepository = remember(database) {
        FavouritesRepository(
            wolneLekturyDatabse = database,
            userId = 1
        )
    }
    val booksRepository = remember(database) {
        BooksRepository(database)
    }
    PreComposeApp {
        BooksAppTheme {
            val navigator = rememberNavigator()
            NavHost(
                navigator = navigator,
                navTransition = NavTransition(),
                initialRoute = "/books"
            ) {
                booksScene(
                    booksRepository = booksRepository,
                    favouritesRepository = favouritesRepository,
                    moveToFavourites = {
                        navigator.navigate(FAVOURITES_ROUTE)
                    }
                )
                favouritesScene(
                    favouritesRepository = favouritesRepository,
                    booksRepository = booksRepository,
                    moveBack = {
                        navigator.goBack()
                    }
                )
            }
        }
    }
}