package com.jstarczewski.booksapp

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.resources.Resource
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

val client by lazy {
    HttpClient {
        engine {
            pipelining = true
        }
        developmentMode = true
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(Resources)
        defaultRequest {
            url {
                host = "wolnelektury.pl"
                protocol = URLProtocol.HTTPS
            }
        }
    }
}

@Resource("/api/books")
class BooksResource

@Resource("/api/authors")
class AuthResource

@Serializable
data class BookApiModel(
    val author: String,
    val title: String,
    val full_sort_key: String,
    val simple_thumb: String,
    val epoch: String
)

@Serializable
data class AuthorApiModel(
    val name: String,
    val slug: String
)

private val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

suspend fun books(): List<BookApiModel> = client.get(BooksResource()).bodyAsText().let {
    json.decodeFromString(ListSerializer(BookApiModel.serializer()), it)
}

suspend fun authors(): List<AuthorApiModel> = client.get(AuthResource()).bodyAsText().let {
    json.decodeFromString(ListSerializer(AuthorApiModel.serializer()), it)
}

class ApiClient

suspend fun ApiClient.getBooks() = client.get(BooksResource()).bodyAsText().let {
    json.decodeFromString(ListSerializer(BookApiModel.serializer()), it)
}

fun ApiClient.booksFlow() = flow { emit(getBooks()) }

