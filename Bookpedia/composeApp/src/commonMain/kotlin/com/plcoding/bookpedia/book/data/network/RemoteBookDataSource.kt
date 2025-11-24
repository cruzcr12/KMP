package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

/**
 * This interface works as an abstraction layer between the data layer and the domain layer.
 * It allows us to change the implementation of the data layer without changing the domain layer.
 */
interface RemoteBookDataSource {
    /**
     * This function will search for books by the given query.
     * @param query The query to search for.
     * @param resultLimit The maximum number of results to return.
     * The result uses the DTO model to avoid using the domain model directly, which is the one that will be used in the presentation layer.
     */
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>
}