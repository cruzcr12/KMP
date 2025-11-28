package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.Serializable

/**
 * As the API we are using is not consistent with the description of the books, because for some
 * books the description is a JSON object and for other is a string, we need to use a custom
 * serializer to handle both cases.
 */

@Serializable(with = BookWorkDtoSerializer::class)
data class BookWorkDto(
    val description: String? = null,
)
