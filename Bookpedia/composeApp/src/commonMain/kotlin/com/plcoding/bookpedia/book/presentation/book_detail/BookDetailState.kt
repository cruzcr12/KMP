package com.plcoding.bookpedia.book.presentation.book_detail

import com.plcoding.bookpedia.book.domain.Book

data class BookDetailState(
    val book: Book? = null,
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false
)