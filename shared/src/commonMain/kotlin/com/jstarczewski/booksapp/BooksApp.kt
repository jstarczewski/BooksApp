package com.jstarczewski.booksapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jstarczewski.booksapp.books.BooksRepository
import com.jstarczewski.booksapp.books.booksScene
import com.jstarczewski.booksapp.details.DetailsRepository
import com.jstarczewski.booksapp.details.detailsPath
import com.jstarczewski.booksapp.details.detailsScene
import com.jstarczewski.booksapp.favourites.favouritesScene
import com.jstarczewski.booksapp.user.USER_ROUTE
import com.jstarczewski.booksapp.user.userScene
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun BooksApp(appComponent: AppComponent) {

    val isSyncing by appComponent
        .dataSynchronizer
        .isSyncing
        .collectAsState(true)

    if (isSyncing) {
        Box(Modifier.fillMaxSize().background(Color.White)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        PreComposeApp {
            BooksAppTheme {
                val navigator = rememberNavigator()
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navigator = navigator,
                    navTransition = NavTransition(),
                    initialRoute = "/books"
                ) {
                    with(appComponent) {
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
    }
}