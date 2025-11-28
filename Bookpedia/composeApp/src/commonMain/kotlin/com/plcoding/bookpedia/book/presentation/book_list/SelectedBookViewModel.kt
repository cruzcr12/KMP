package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import com.plcoding.bookpedia.book.domain.Book
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The purpose of this view model is to share data between several views
 * and avoid to pass data through parameters in the navigation  or make unnecessary calls to the API
 */
class SelectedBookViewModel: ViewModel() {
    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook = _selectedBook

    fun onSelectBook(book: Book?) {
        _selectedBook.value = book
    }

}