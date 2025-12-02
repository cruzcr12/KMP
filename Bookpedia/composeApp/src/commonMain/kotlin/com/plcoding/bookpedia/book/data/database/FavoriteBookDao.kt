package com.plcoding.bookpedia.book.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {

    // This function will insert or update a book in the database.
    @Upsert
    suspend fun upsertFavoriteBook(book: BookEntity)

    // This function will retrieve a list of favorite books from the database.
    // The Flow type allows us to get notified when the data in the database changes, and this
    // doesn't require to be suspend because it is asynchronous by default.
    @Query("SELECT * FROM BookEntity")
    fun getFavoriteBooks(): Flow<List<BookEntity>>

    // This function will retrieve a specific book from the database
    @Query("SELECT * FROM BookEntity WHERE id = :id")
    suspend fun getFavoriteBookById(id: String): BookEntity?

    // This function will delete a specific book from the database
    @Query("DELETE FROM BookEntity WHERE id = :id")
    suspend fun deleteFavoriteBook(id: String)
}