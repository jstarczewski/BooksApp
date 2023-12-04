package com.jstarczewski.booksapp

import androidx.compose.runtime.Composable
import com.jstarczewski.booksapp.books.BooksRepository
import com.jstarczewski.booksapp.details.DetailsRepository
import com.jstarczewski.booksapp.favourites.FavouritesRepository
import com.jstarczewski.booksapp.shared.api.ApiClient
import com.jstarczewski.booksapp.shared.synchronizer.DataSynchronizer
import com.jstarczewski.booksapp.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class AppComponent(
    @get:Provides val wolneLekturyDatabse: WolneLekturyDatabse,
    @get: Provides val dataSynchronizer: DataSynchronizer,
    @get: Provides val apiClient: ApiClient
) {

    abstract val booksRepository: BooksRepository

    abstract val favouritesRepository: FavouritesRepository

    abstract val detailsRepository: DetailsRepository

    abstract val userRepository: UserRepository

    val ioDispatcher: CoroutineDispatcher
        @Provides
        get() = Dispatchers.IO
}

@Composable
expect fun createAppComponent(): AppComponent