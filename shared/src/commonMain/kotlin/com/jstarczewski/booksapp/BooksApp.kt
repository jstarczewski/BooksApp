package com.jstarczewski.booksapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jstarczewski.booksapp.books.BooksRepository
import com.jstarczewski.booksapp.books.booksScene
import com.jstarczewski.booksapp.details.DetailsRepository
import com.jstarczewski.booksapp.details.detailsPath
import com.jstarczewski.booksapp.details.detailsScene
import com.jstarczewski.booksapp.favourites.FavouritesRepository
import com.jstarczewski.booksapp.favourites.favouritesScene
import com.jstarczewski.booksapp.shared.db.WolneLekturyDatabase
import com.jstarczewski.booksapp.user.USER_ROUTE
import com.jstarczewski.booksapp.user.UserRepository
import com.jstarczewski.booksapp.user.userScene
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
    val userRepository = remember(database) {
        UserRepository(database)
    }
    val detailsRepository = remember(database) {
        DetailsRepository(database)
    }
    PreComposeApp {
        BooksAppTheme {
            val navigator = rememberNavigator()
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navigator = navigator,
                navTransition = NavTransition(),
                initialRoute = "/books"
            ) {
                booksScene(
                    booksRepository = booksRepository,
                    favouritesRepository = favouritesRepository,
                    moveToUser = {
                        navigator.navigate(USER_ROUTE)
                    },
                    moveToFavourites = {
                        navigator.navigate(detailsPath(it))
                    }
                )
                favouritesScene(
                    favouritesRepository = favouritesRepository,
                    booksRepository = booksRepository,
                    moveBack = {
                        navigator.goBack()
                    }
                )
                userScene(
                    userRepository
                )
                detailsScene(
                    detailsRepository = detailsRepository
                )
            }
        }
    }
}