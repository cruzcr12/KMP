package com.plcoding.bookpedia.book.app

import kotlinx.serialization.Serializable

/**
 * Interface that defines the routes of the application
 */
sealed interface Route {
    @Serializable
    data object BookGraph: Route

    @Serializable
    data object BookList: Route

    @Serializable
    data class BookDetail(val bookId: String): Route


}