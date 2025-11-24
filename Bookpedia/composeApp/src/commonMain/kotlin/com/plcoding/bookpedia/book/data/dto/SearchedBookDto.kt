package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The idea of having a DTO (Data Transform Object) is to separate the data layer from the domain layer.
 * If anything changes in the data layer, we don't want to break the domain layer.
 * For example, if the "key" value changes in the JSON response, we don't need to change anything in the domain or presentation layer.
 */
@Serializable
data class SearchedBookDto(
    @SerialName("key") val id: String,
    @SerialName("title") val title: String,
    @SerialName("language") val languages: List<String>? = null,
    @SerialName("cover_i") val coverAlternativeKey: Int? = null,
    @SerialName("author_key") val authorKeys: List<String>? = null,
    @SerialName("author_name") val authorNames: List<String>? = null,
    @SerialName("cover_edition_key") val coverKey: String? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    @SerialName("ratings_average") val ratingsAverage: Double? = null,
    @SerialName("ratings_count") val ratingsCount: Int? = null,
    @SerialName("number_of_pages_median") val numPagesMedian: Int? = null,
    @SerialName("edition_count") val numEditions: Int? = null,
)