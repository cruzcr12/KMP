package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book

/**
 * This class will map the data model into a domain model
 * The domain model is the one that will be used in the presentation layer
 */
fun SearchedBookDto.toBook(): Book {
    return Book(
        id = id,
        title = title,
        imageUrl = if(coverKey != null) {
            "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
        }else {
            "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"
        },
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishYear = firstPublishYear.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian ?: 0,
        numEditions = numEditions ?: 0
    )
}