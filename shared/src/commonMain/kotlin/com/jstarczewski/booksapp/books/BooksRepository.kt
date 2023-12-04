package com.jstarczewski.booksapp.books

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jstarczewski.booksapp.shared.api.ApiClient
import com.jstarczewski.booksapp.Book
import com.jstarczewski.booksapp.shared.api.BookApiModel
import com.jstarczewski.booksapp.SelectAuthorsWithMostBooks
import com.jstarczewski.booksapp.WolneLekturyDatabse
import com.jstarczewski.booksapp.shared.api.authors
import com.jstarczewski.booksapp.shared.api.books
import com.jstarczewski.booksapp.shared.api.getBooks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class DomainBook(
    val key: String,
    val name: String,
    val author: String,
    val thumbnailUrl: String?,
    val epoch: String?
)

fun Book.asDomainBook(): DomainBook = let {
    DomainBook(
        key = it.full_sort_key,
        name = it.title,
        author = it.author,
        thumbnailUrl = it.ThumbnailUrl,
        epoch = it.Epoch
    )
}

fun BookApiModel.asDomainBook(): DomainBook = let {
    DomainBook(
        key = it.full_sort_key,
        name = it.title,
        author = it.author,
        thumbnailUrl = it.simple_thumb,
        epoch = it.epoch
    )
}

fun BookApiModel.asBook() = let {
    Book(
        full_sort_key = it.full_sort_key,
        title = it.title,
        author = it.author,
        ThumbnailUrl = it.simple_thumb,
        Epoch = it.epoch
    )
}

interface Synchronizer {

    fun CoroutineScope.sync(
        interval: Duration
    ): Job

    suspend fun syncNow()

    suspend fun syncIfOlderThan(
        duration: Duration = 20.minutes
    )
}

class BooksSynchronizer(
    private val db: WolneLekturyDatabse,
    private val apiClient: ApiClient
) : Synchronizer {

    override fun CoroutineScope.sync(
        interval: Duration
    ) = launch {
        while (isActive) {
            withContext(Dispatchers.Default) {
                apiClient.getBooks().forEach {
                    db.bookQueries.insert(
                        full_sort_key = it.full_sort_key,
                        author = it.author,
                        title = it.title,
                        ThumbnailUrl = it.simple_thumb,
                        Epoch = it.epoch
                    )
                }
            }
            delay(interval)
        }
    }

    override suspend fun syncNow() {
        withContext(Dispatchers.Default) {
            apiClient.getBooks().forEach {
                db.bookQueries.insert(
                    full_sort_key = it.full_sort_key,
                    author = it.author,
                    title = it.title,
                    ThumbnailUrl = it.simple_thumb,
                    Epoch = it.epoch
                )
            }
        }
    }

    override suspend fun syncIfOlderThan(
        duration: Duration
    ) = withContext(Dispatchers.IO) {
        val syncs = db.bookQueries.selectLastestSync(table_key = "book")
            .executeAsList()


        when {
            syncs.isEmpty() -> {
                books().onEach {
                    db.bookQueries.insertFullBookObject(
                        it.asBook()
                    )
                }

                db.bookQueries.insertIntoSync(
                    table_key = "book",
                    timestamp = Clock.System.now().toEpochMilliseconds().toString()
                )
            }

            syncs.size > 1 -> {
                throw IllegalStateException()
            }

            else -> {
                println("997IGI Getting books from DB or API")
                val stamp = syncs.first()
                val instant = Instant.fromEpochMilliseconds(stamp.timestamp.toLong())

                if (Clock.System.now().minus(duration) > instant) {
                    println("997IGI Getting from API")
                    books().onEach {
                        db.bookQueries.insertFullBookObject(
                            it.asBook()
                        )
                    }
                    db.bookQueries.insertIntoSync(
                        table_key = "book",
                        timestamp = Clock.System.now().toEpochMilliseconds().toString()
                    )
                }
            }
        }
    }
}

@Inject
class BooksRepository(
    private val db: WolneLekturyDatabse
) {

    val books by lazy {
        db.bookQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map {
                it.map { it.asDomainBook() }
            }
    }

    fun selectFrom(
        keys: List<String>
    ) = db.bookQueries.selectFrom(keys).asFlow().mapToList(Dispatchers.Default)
        .map {
            it.map {
                it.asDomainBook()
            }
        }

    suspend fun download() {
        val b = books()
        b.forEach {
            db.bookQueries.insertFullBookObject(
                it.asBook()
            )
        }
    }

    fun getAll(): Flow<List<SelectAuthorsWithMostBooks>> =
        db.bookQueries.selectAuthorsWithMostBooks().asFlow()
            .mapToList(Dispatchers.Default)

    fun getAllQueried(): Flow<List<SelectAuthorsWithMostBooks>> =
        db.bookQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map {
                it.groupBy { it.author }
                    .map { it.key to it.value.size.toLong() }
                    .sortedBy { it.second }.reversed().map {
                        SelectAuthorsWithMostBooks(it.first, it.second)
                    }
            }

    suspend fun setAuthros() {
        authors().map {
            db.bookQueries.insertAuthor(it.name, it.slug)
        }
    }
}