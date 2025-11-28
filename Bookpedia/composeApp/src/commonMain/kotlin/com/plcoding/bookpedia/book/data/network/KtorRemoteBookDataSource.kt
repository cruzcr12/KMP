package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.data.safeApiCall
import com.plcoding.bookpedia.core.domain.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://openlibrary.org"

/**
 * The idea of having a DTO (Data Transform Object) is to separate the data layer from the domain layer.
 * If the http library changes in the future, we just need to change the implementation of the interface.
 */
class KtorRemoteBookDataSource(
    private val httpClient: HttpClient,
): RemoteBookDataSource {
    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?
    ): Result<SearchResponseDto, DataError.Remote>{
        return safeApiCall {
            httpClient.get(
                urlString = "$BASE_URL/search.json"
            ) {
                parameter("q", query)
                parameter("limit", resultLimit)
                parameter("language", "eng")
                parameter("fields", "key,title,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count")
            }
        }
    }
}