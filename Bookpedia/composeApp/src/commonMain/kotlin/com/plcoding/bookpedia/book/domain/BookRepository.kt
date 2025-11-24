package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

/**
 * This is an abstraction layer between the data layer and the domain layer.
 * It allows us to change the implementation of the data layer without changing the domain layer.
 * Domain layer is not allowed to access anything outside the domain layer.
 */
interface BookRepository {
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
}