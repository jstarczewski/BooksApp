package com.jstarczewski.booksapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.jstarczewski.booksapp.books.booksScene
import com.jstarczewski.booksapp.details.detailsPath
import com.jstarczewski.booksapp.details.detailsScene
import com.jstarczewski.booksapp.favourites.FAVOURITES_ROUTE
import com.jstarczewski.booksapp.favourites.favouritesScene
import com.jstarczewski.booksapp.user.USER_ROUTE
import com.jstarczewski.booksapp.user.userScene
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksApp(appComponent: AppComponent) {
    BooksAppTheme {
        PreComposeApp {
            val scrollBehavior =
                TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

            val isSyncing by appComponent
                .dataSynchronizer
                .isSyncing
                .collectAsState(true)

            val navigator = rememberNavigator()
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        colors = TopAppBarDefaults.topAppBarColors(),
                        title = {
                            Text("Books")
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        actions = {
                            IconButton(onClick = {
                                navigator.navigate(FAVOURITES_ROUTE)
                            }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite, contentDescription = null
                                )
                            }
                            IconButton(onClick = {
                                navigator.navigate(USER_ROUTE)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null
                                )
                            }
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            }
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {},
                                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                            ) {
                                if (isSyncing) {
                                    CircularProgressIndicator()
                                } else {
                                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                                }
                            }
                        }
                    )
                }
            ) { padding ->
                NavHost(
                    modifier = Modifier.fillMaxSize().padding(padding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    navigator = navigator,
                    navTransition = NavTransition(),
                    initialRoute = "/books"
                ) {
                    with(appComponent) {
                        booksScene(
                            booksRepository = booksRepository,
                            favouritesRepository = favouritesRepository,
                            moveToDetails = {
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