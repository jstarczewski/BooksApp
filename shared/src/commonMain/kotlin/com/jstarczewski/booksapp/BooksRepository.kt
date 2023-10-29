package com.jstarczewski.booksapp

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class DomainBook(
    val name: String,
    val author: String
)

interface Synchronizer {

    fun CoroutineScope.sync(
        interval: Duration
    ): Job
}

class BooksSynchronizer(
    private val db: WolneLekturyDatabse,
    private val apiClient: ApiClient
) : Synchronizer {

    override fun CoroutineScope.sync(
        interval: Duration
    ) = launch {
        while (isActive) {
            println("Syncing now ${Clock.System.now()}")
            withContext(Dispatchers.Default) {
                apiClient.getBooks().forEach {
                    db.bookQueries.insert(
                        full_sort_key = it.full_sort_key,
                        author = it.author,
                        title = it.title
                    )
                }
            }
            delay(interval)
        }
    }
}

class BooksRepository(
    private val db: WolneLekturyDatabse
) {

    val books by lazy {
        db.bookQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map {
                it.map {
                    DomainBook(
                        name = it.title,
                        author = it.author
                    )
                }
            }
    }

    val booksFromApi by lazy {
        flow {
            emit(
                books().map {
                    DomainBook(
                        name = it.title,
                        author = it.author
                    )
                }
            )
        }.flowOn(Dispatchers.Default)
    }

    suspend fun download() {
        val b = books()
        b.forEach {
            db.bookQueries.insertFullBookObject(
                Book(
                    full_sort_key = it.full_sort_key,
                    title = it.title,
                    author = it.author
                )
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

    suspend fun getLatestBooks(): Flow<List<DomainBook>> {

        val syncs = db.bookQueries.selectLastestSync(table_key = "book")
            .executeAsList()

        println("997IGI Getting latest books")

        when {
            syncs.isEmpty() -> {
                println("997IGI No books, storing")
                books().onEach {
                    db.bookQueries.insert(
                        full_sort_key = it.full_sort_key,
                        author = it.author,
                        title = it.title
                    )
                }

                db.bookQueries.insertIntoSync(
                    table_key = "book",
                    timestamp = Clock.System.now().toEpochMilliseconds().toString()
                )

                return db.bookQueries.selectAll()
                    .asFlow()
                    .mapToList(Dispatchers.Default)
                    .map {
                        it.map {
                            DomainBook(
                                name = it.title,
                                author = it.author
                            )
                        }
                    }
            }

            syncs.size > 1 -> {
                throw IllegalStateException()
            }

            else -> {

                println("997IGI Getting books from DB or API")
                val stamp = syncs.first()

                val instant = Instant.fromEpochMilliseconds(stamp.timestamp.toLong())

                return if (Clock.System.now().minus(1.minutes) > instant) {
                    println("997IGI Getting from API")
                    books().onEach {
                        db.bookQueries.insert(
                            full_sort_key = it.full_sort_key,
                            author = it.author,
                            title = it.title
                        )
                    }

                    db.bookQueries.insertIntoSync(
                        table_key = "book",
                        timestamp = Clock.System.now().toEpochMilliseconds().toString()
                    )

                    return db.bookQueries.selectAll()
                        .asFlow()
                        .mapToList(Dispatchers.Default)
                        .map {
                            it.map {
                                DomainBook(
                                    name = it.title,
                                    author = it.author
                                )
                            }
                        }
                } else {
                    println("997IGI Getting from DB")
                    db.bookQueries.selectAll()
                        .asFlow()
                        .mapToList(Dispatchers.Default)
                        .map {
                            it.map {
                                DomainBook(
                                    name = it.title,
                                    author = it.author
                                )
                            }
                        }
                }

            }
        }
    }
}